package com.princess.shopapi.helpers

import com.princess.shopapi.dto.UserDTO
import com.princess.shopapi.model.UserEntity

fun UserEntity.toUserResponse(): UserDTO = UserDTO(
    id = this.id ?: throw IllegalArgumentException("User ID is required."),
    name = this.name,
    username = this.username,
    role = this.role
)

fun UserDTO.createUserEntity(): UserEntity = UserEntity(
    name = this.name,
    username = this.username,
    password = this.password ?: throw kotlin.IllegalArgumentException("Password is required.")
)