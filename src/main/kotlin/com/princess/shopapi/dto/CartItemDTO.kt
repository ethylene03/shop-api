package com.princess.shopapi.dto

import java.util.UUID

data class CartItemDTO(
    val id: UUID? = null,
    val cartId: UUID,
    val product: ProductDTO,
    val quantity: Int
)
