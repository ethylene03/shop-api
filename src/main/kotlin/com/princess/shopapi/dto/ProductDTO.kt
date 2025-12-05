package com.princess.shopapi.dto

import java.util.UUID

data class ProductDTO(
    val id: UUID? = null,
    val name: String,
    val price: Double,
    val description: String? = "",
    val quantity: Int = 1
)

data class QuantityDTO(
    val quantity: Int
)
