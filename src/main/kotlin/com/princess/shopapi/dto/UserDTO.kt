package com.princess.shopapi.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.*

data class UserDTO(
    val id: UUID? = null,
    @field:NotNull(message = "Role is required.")
    val role: Role,
    @field:NotBlank(message = "Name is required.")
    val name: String,
    @field:NotBlank(message = "Username is required.")
    val username: String,
    val password: String? = null,
    val cart: CartDTO? = null,
)

data class CredentialsDTO(
    @field:NotBlank(message = "Username is required.")
    val username: String,
    @field:NotBlank(message = "Password is required.")
    val password: String
)

data class UserTokenDTO(
    val id: UUID? = null,
    val name: String = "",
    val username: String = "",
    val cart: CartDTO? = null,
    val role: Role = Role.BUYER,
    val token: String = ""
)

enum class Role {
    BUYER,
    SELLER
}