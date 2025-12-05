package com.princess.shopapi.dto

import java.util.UUID

data class OrderItemDTO(
    val id: UUID? = null,
    val orderId: UUID,
    val product: ProductDTO,
    val quantity: Int,
    val priceAtPurchase: Double
)
