package com.princess.shopapi.service

import com.princess.shopapi.dto.OrderDTO
import com.princess.shopapi.dto.OrderStatus
import com.princess.shopapi.dto.Role
import com.princess.shopapi.helpers.ResourceNotFoundException
import com.princess.shopapi.helpers.UnauthorizedUserException
import com.princess.shopapi.helpers.createOrderItemEntity
import com.princess.shopapi.helpers.toOrderResponse
import com.princess.shopapi.model.OrderEntity
import com.princess.shopapi.model.OrderItemEntity
import com.princess.shopapi.repository.OrderRepository
import com.princess.shopapi.repository.ProductRepository
import com.princess.shopapi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
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

    fun create(userId: UUID, details: OrderDTO): List<OrderDTO> {
        log.debug("Fetching buyer..")
        val buyer = userRepository.findById(userId).orElseThrow {
            log.error("User does not exist.")
            ResourceNotFoundException("User does not exist.")
        }

        log.debug("Grouping order items by seller..")
        val groupedOrderItems = details.products.groupBy { it.product.seller }

        log.debug("Creating orders..")
        val orders = groupedOrderItems.map { (seller, orderItems) ->
            val sellerEntity = seller?.let { userRepository.findById(seller).orElseThrow() }
                ?: throw IllegalArgumentException("One or more order items have no seller ID passed.")

            val order = OrderEntity(
                buyer = buyer,
                seller = sellerEntity,
                status = details.status,
                totalAmount = orderItems.sumOf { it.priceAtPurchase * it.quantity },
            )

            order.items = orderItems.map { item ->
                val product = productRepository.findById(
                    item.product.id ?: throw IllegalArgumentException("Product ID is required.")
                ).orElseThrow()

                log.debug("Checking if product is not deleted..")
                if(product.isDeleted) {
                    log.error("Product is deleted, cannot place this order.")
                    throw IllegalArgumentException("Deleted products cannot be purchase.")
                }

                log.debug("Updating product quantity..")
                productRepository.save(product.apply { quantity -= item.quantity })

                item.createOrderItemEntity(product, order)
            }.toMutableList()

            order
        }

        log.debug("Saving orders..")
        return orders.map { repository.save(it).toOrderResponse() }
    }

    fun findAll(userId: UUID, role: Role, pageable: Pageable): Page<OrderDTO> {
        val customPageable = if (pageable.sort.isUnsorted) PageRequest.of(
            pageable.pageNumber,
            pageable.pageSize,
            Sort.by(Sort.Direction.DESC, "createdAt")
        ) else pageable

        log.debug("Finding all orders..")
        val page = when (role) {
            Role.BUYER -> repository.findAllByBuyerId(userId, customPageable)
            Role.SELLER -> repository.findAllBySellerId(userId, customPageable)
        }

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
        if (order.buyerId != userId && order.sellerId != userId) {
            log.error("User is not authorized to query this order.")
            throw UnauthorizedUserException("User is not authorized to query this order.")
        }

        return order
    }

    fun updateStatus(orderId: UUID, status: OrderStatus, userId: UUID): OrderDTO {
        if (status == OrderStatus.CANCELED) return cancelOrder(orderId)

        log.debug("Finding order..")
        val order = repository.findById(orderId).orElseThrow {
            log.error("Order does not exist.")
            ResourceNotFoundException("Order does not exist.")
        }

        log.debug("Checking user authorization..")
        if (order.seller?.id != userId)
            throw UnauthorizedUserException("User is not authorized to update this order.")

        log.debug("Disallow status update if delivered or canceled")
        if (order.status == OrderStatus.DELIVERED || order.status == OrderStatus.CANCELED) {
            log.error("Cannot change status if it is currently delivered/canceled.")
            throw IllegalArgumentException("Changing status if it is currently delivered/canceled is not allowed.")
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
            log.debug("Disallow cancel order if delivered or canceled")
            if (order.status == OrderStatus.DELIVERED || order.status == OrderStatus.CANCELED) {
                log.error("Cannot cancel order if it is currently delivered/canceled.")
                throw IllegalArgumentException("Changing status if it is currently delivered/canceled is not allowed.")
            }

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