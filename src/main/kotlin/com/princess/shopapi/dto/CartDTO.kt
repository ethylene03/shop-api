package com.princess.shopapi.dto

import java.util.UUID

data class CartDTO(
    val id: UUID? = null,
    val userId: UUID,
    val totalAmount: Double,
    val products: MutableList<CartItemDTO>
)
