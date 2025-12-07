package com.princess.shopapi.dto

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range
import java.util.*

data class CartDTO(
    val id: UUID? = null,
    @field:NotNull(message = "User ID can't be empty.")
    val userId: UUID,
    @field:Range(min = 0, message = "totalAmount should be positive.")
    val totalAmount: Double,
    val products: MutableList<CartItemDTO>
)
