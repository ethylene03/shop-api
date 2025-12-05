package com.princess.shopapi.dto

import java.util.UUID

data class UserDTO(
    val id: UUID? = null,
    val role: Role,
    val name: String,
    val username: String,
    val password: String? = null,
    val cart: CartDTO? = null,
    val orders: MutableList<OrderDTO> = mutableListOf()
)

data class CredentialsDTO(
    val username: String,
    val password: String
)

data class UserTokenDTO(
    val id: UUID? = null,
    val name: String = "",
    val username: String = "",
    val cart: CartDTO? = null,
    val orders: MutableList<OrderDTO>,
    val role: Role = Role.BUYER,
    val token: String = ""
)

enum class Role {
    BUYER,
    SELLER
}