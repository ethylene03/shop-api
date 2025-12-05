package com.princess.shopapi.service

import com.princess.shopapi.dto.CartDTO
import com.princess.shopapi.dto.CartItemDTO
import com.princess.shopapi.helpers.ResourceNotFoundException
import com.princess.shopapi.helpers.createCartItemEntity
import com.princess.shopapi.helpers.toCartItemResponse
import com.princess.shopapi.repository.CartItemRepository
import com.princess.shopapi.repository.CartRepository
import com.princess.shopapi.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CartItemService(private val repository: CartItemRepository, private val productRepository: ProductRepository, private val cartRepository: CartRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun create(details: CartItemDTO): CartItemDTO {
        log.debug("Checking if cart-item exists..")
        repository.findByCartIdAndProductId(details.cartId, details.product.id) ?.let {
            log.error("CartItem exists.")
            throw IllegalArgumentException("CartItem exists, try updating instead.")
        }

        log.debug("Fetching product..")
        val product = details.product.id
            ?.let { productRepository.findById(it).orElseThrow() }
            ?: run {
                log.error("Product ID is required.")
                throw IllegalArgumentException("Product ID is required.")
            }

        log.debug("Fetching cart..")
        val cart = cartRepository.findById(details.cartId).orElseThrow()

        log.debug("Saving cart-item..")
        return details.createCartItemEntity(product, cart)
            .let { repository.save(it) }
            .toCartItemResponse()
    }

    fun update(itemId: UUID, details: CartItemDTO): CartItemDTO {
        log.debug("Checking if cart-item exists..")
        val cartItem = repository.findById(itemId)
            .orElseThrow {
                log.error("CartItem does not exist.")
                ResourceNotFoundException("CartItem does not exist.")
            }

        log.debug("Saving cart-item..")
        return cartItem.apply { quantity = details.quantity }
            .let { repository.save(it) }
            .toCartItemResponse()
    }

    fun delete(itemId: UUID) {
        log.debug("Checking if cart-item exists..")
        repository.findById(itemId)
            .orElseThrow {
                log.error("CartItem does not exist.")
                ResourceNotFoundException("CartItem does not exist.")
            }

        log.debug("Deleting cart-item..")
        repository.deleteById(itemId)
    }
}