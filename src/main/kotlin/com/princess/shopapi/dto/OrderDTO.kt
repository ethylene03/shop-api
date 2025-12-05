package com.princess.shopapi.dto

import java.util.UUID

data class OrderDTO(
    val id: UUID? = null,
    val userId: UUID,
    val status: OrderStatus,
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


