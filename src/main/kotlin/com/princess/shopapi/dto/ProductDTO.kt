package com.princess.shopapi.dto

import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Range
import java.util.*

data class ProductDTO(
    val id: UUID? = null,
    @field:NotBlank(message = "Name is required.")
    val name: String,
    @field:Range(min = 0, message = "Price should be positive.")
    val price: Double,
    val description: String? = "",
    @field:Range(min = 0, message = "Quantity should be positive.")
    val quantity: Int = 1,
    val seller: UUID? = null,
    val isDeleted: Boolean = false,
)

data class QuantityDTO(
    @field:Range(min = 0, message = "Quantity should be positive.")
    val quantity: Int
)
