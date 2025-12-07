package com.princess.shopapi.service

import com.princess.shopapi.dto.CartItemDTO
import com.princess.shopapi.helpers.ResourceNotFoundException
import com.princess.shopapi.helpers.createCartItemEntity
import com.princess.shopapi.helpers.toCartItemResponse
import com.princess.shopapi.repository.CartItemRepository
import com.princess.shopapi.repository.CartRepository
import com.princess.shopapi.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

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

        log.debug("Creating cart-item..")
        val cartItem = details.createCartItemEntity(product, cart)

        log.debug("Updating cart..")
        return cart.apply {
            items.add(cartItem)
            totalAmount = items.sumOf { (it.product?.price ?: 0.0) * it.quantity }
        }.let { cartRepository.save(it) }
            .items.first { it.product?.id == cartItem.product?.id }
            .toCartItemResponse()
    }

    fun update(itemId: UUID, details: CartItemDTO): CartItemDTO {
        log.debug("Checking if cart-item exists..")
        val cartItem = repository.findById(itemId)
            .orElseThrow {
                log.error("CartItem does not exist.")
                ResourceNotFoundException("CartItem does not exist.")
            }

        log.debug("Checking if cart exists..")
        val cart = cartItem.cart ?: throw ResourceNotFoundException("Cart does not exist.")

        log.debug("Updating cart-item..")
        val item = cart.items.find { it.id == itemId }
            ?: throw ResourceNotFoundException("CartItem does not exist.")
        item.quantity = details.quantity

        return cart.apply {
            log.debug("Updating total amount..")
            totalAmount = items.sumOf { (it.product?.price ?: 0.0) * it.quantity }
        }.let {
            log.debug("Saving cart..")
            cartRepository.save(it)
        }.items.first { it.product?.id == cartItem.product?.id }
            .toCartItemResponse()
    }

    fun delete(itemId: UUID) {
        log.debug("Checking if cart-item exists..")
        val cart = repository.findById(itemId)
            .orElseThrow {
                log.error("CartItem does not exist.")
                ResourceNotFoundException("CartItem does not exist.")
            }.cart ?: throw ResourceNotFoundException("Cart does not exist.")

        log.debug("Deleting cart-item..")
        cart.apply {
            items.removeIf { it.id == itemId }
        }.let { cartRepository.save(it) }
    }
}