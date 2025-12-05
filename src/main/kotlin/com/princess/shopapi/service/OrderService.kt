package com.princess.shopapi.service

import com.princess.shopapi.dto.OrderDTO
import com.princess.shopapi.dto.OrderStatus
import com.princess.shopapi.helpers.ResourceNotFoundException
import com.princess.shopapi.helpers.createOrderEntity
import com.princess.shopapi.helpers.createOrderItemEntity
import com.princess.shopapi.helpers.toOrderResponse
import com.princess.shopapi.model.OrderItemEntity
import com.princess.shopapi.repository.OrderRepository
import com.princess.shopapi.repository.ProductRepository
import com.princess.shopapi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class OrderService(
    private val repository: OrderRepository,
    private val userRepository: UserRepository,
    private val productRepository: ProductRepository
) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun create(userId: UUID, details: OrderDTO): OrderDTO {
        log.debug("Fetching user..")
        val user = userRepository.findById(userId).orElseThrow {
            log.error("User does not exist.")
            ResourceNotFoundException("User does not exist.")
        }

        log.debug("Creating order..")
        val order = details.createOrderEntity(user)

        log.debug("Creating each OrderItem..")
        details.products.forEach { orderItem ->
            val product = orderItem.product.id?.let { productRepository.findById(it).orElseThrow() }
                ?: throw ResourceNotFoundException("One or more of the given products does not exist.")
            val item = orderItem.createOrderItemEntity(product, order)
            order.items.add(item)
        }

        log.debug("Saving order..")
        return order.apply {
            totalAmount = order.items.sumOf { it.priceAtPurchase * it.quantity }
        }.let { repository.save(it) }.toOrderResponse()
    }

    fun findAll(userId: UUID, pageable: Pageable): Page<OrderDTO> {
        log.debug("Finding all orders of user..")
        val page = repository.findAllByUserId(userId, pageable)

        val list = page.content.map { it.toOrderResponse() }
        return PageImpl(
            list,
            page.pageable,
            page.totalElements
        )
    }

    fun find(orderId: UUID, userId: UUID): OrderDTO {
        log.debug("Finding order..")
        val order = repository.findById(orderId).orElseThrow {
            log.error("Order does not exist.")
            ResourceNotFoundException("Order does not exist.")
        }.toOrderResponse()

        log.debug("Checking if user is owner..")
        if(order.userId != userId) {
            log.error("User is not authorized to query this order.")
            throw IllegalArgumentException("User is not authorized to query this order.")
        }

        return order
    }

    fun updateStatus(orderId: UUID, status: OrderStatus): OrderDTO {
        if (status == OrderStatus.CANCELED) return cancelOrder(orderId)

        log.debug("Finding order..")
        val order = repository.findById(orderId).orElseThrow {
            log.error("Order does not exist.")
            ResourceNotFoundException("Order does not exist.")
        }

        return order.apply {
            this.status = status
        }.let { repository.save(it) }.toOrderResponse()
    }

    fun cancelOrder(orderId: UUID): OrderDTO {
        log.debug("Finding order..")
        val order = repository.findById(orderId).orElseThrow {
            log.error("Order does not exist.")
            ResourceNotFoundException("Order does not exist.")
        }

        log.debug("Updating status..")
        val sevenDaysAgo = LocalDateTime.now().minusDays(7)
        return if (order.createdAt?.isAfter(sevenDaysAgo) ?: false) {
            log.debug("Returning all items to product..")
            order.items.forEach { item: OrderItemEntity ->
                item.product?.apply {
                    this.quantity += item.quantity
                }?.let { productRepository.save(it) }
            }

            log.debug("Saving changes..")
            order.apply { status = OrderStatus.CANCELED }
                .let { repository.save(it) }
                .toOrderResponse()
        } else {
            log.error("Cannot cancel an order after 7 days.")
            throw IllegalArgumentException("Cannot cancel an order after 7 days.")
        }
    }
}