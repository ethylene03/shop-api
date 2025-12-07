package com.princess.shopapi.dto

import jakarta.validation.constraints.NotNull
import org.hibernate.validator.constraints.Range
import java.util.*

data class CartItemDTO(
    val id: UUID? = null,
    @field:NotNull(message = "Shopping cart can't be empty.")
    val cartId: UUID,
    @field:NotNull(message = "Product details can't be empty.")
    val product: ProductDTO,
    @field:Range(min = 1, message = "Amount must be above 1.")
    val quantity: Int
)
