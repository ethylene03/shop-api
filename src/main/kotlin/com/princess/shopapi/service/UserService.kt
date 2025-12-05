package com.princess.shopapi.service

import com.princess.shopapi.dto.Role
import com.princess.shopapi.dto.UserDTO
import com.princess.shopapi.helpers.*
import com.princess.shopapi.model.CartEntity
import com.princess.shopapi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(private val repository: UserRepository, private val passwordManager: PasswordManager) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun create(details: UserDTO): UserDTO {
        log.debug("Checking if username is unique..")
        repository.findByUsername(details.username)
            ?.let {
                log.error("Username already exists.")
                throw DuplicateKeyException("Username already exists.")
            }

        log.debug("Creating user..")
        val user = details.copy(password = passwordManager.hash(details.password))
            .createUserEntity()

        log.debug("Creating cart..")
        user.apply {
            cart = CartEntity(user = this, totalAmount = 0.0, items = mutableListOf())
        }

        return repository.save(user).toUserResponse()
    }

    fun findAll(role: Role, pageable: Pageable): Page<UserDTO> {
        val page = repository.findAllByRole(role, pageable)

        val list = page.content.map { it.toUserResponse() }
        return PageImpl(
            list,
            page.pageable,
            page.totalElements
        )
    }

    fun find(id: UUID): UserDTO {
        return repository.findById(id)
            .orElseThrow {
                log.error("User Id not found.")
                throw ResourceNotFoundException("User does not exist.")
            }.toUserResponse()
    }

    fun update(id: UUID, details: UserDTO): UserDTO {
        log.debug("Checking if username is unique..")
        repository.findByUsername(details.username)
            ?.takeIf { it.id == id }
            ?.let {
                log.error("Username already exists.")
                throw DuplicateKeyException("Username already exists.")
            }

        log.debug("Finding user by given ID..")
        val currentUser = repository.findById(id)
            .orElseThrow {
                log.error("User not found.")
                ResourceNotFoundException("User not found.")
            }

        details.password?.takeUnless { passwordManager.isMatch(it, currentUser.password) }
            ?.run {
                log.error("Credentials is incorrect.")
                throw InvalidCredentialsException("Given credentials is incorrect.")
            }

        return currentUser.apply {
            name = details.name
            username = details.username
        }.run { repository.save(this) }.toUserResponse()
    }

    fun delete(id: UUID) {
        log.debug("Checking if ID exists..")
        find(id)

        log.debug("Deleting data..")
        repository.deleteById(id)
    }
}