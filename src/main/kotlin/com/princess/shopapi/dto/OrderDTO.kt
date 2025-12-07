package com.princess.shopapi.dto

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range
import java.util.*

data class OrderDTO(
    val id: UUID? = null,
    @field:NotNull(message = "User ID can't be empty.")
    val userId: UUID,
    @field:NotNull(message = "Order Status can't be empty.")
    val status: OrderStatus,
    @field:Range(min = 0, message = "totalAmount should be positive.")
    val totalAmount: Double,
    val products: MutableList<OrderItemDTO>,
)

data class OrderStatusDTO(
    val status: OrderStatus
)

enum class OrderStatus {
    PREPARING,
    TO_SHIP,
    SHIPPING,
    DELIVERED,
    CANCELED
}


