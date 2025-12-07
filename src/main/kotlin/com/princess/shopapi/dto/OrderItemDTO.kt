package com.princess.shopapi.dto

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range
import java.util.*

data class OrderItemDTO(
    val id: UUID? = null,
    @field:NotNull(message = "Order ID should not be empty.")
    val orderId: UUID,
    @field:NotNull(message = "Product details should not be empty.")
    val product: ProductDTO,
    @field:Range(min = 0, message = "Quantity should be positive.")
    val quantity: Int,
    @field:Range(min = 0, message = "Price should be positive.")
    val priceAtPurchase: Double
)
