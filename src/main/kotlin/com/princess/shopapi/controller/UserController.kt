package com.princess.shopapi.controller

import com.princess.shopapi.dto.Role
import com.princess.shopapi.dto.UserDTO
import com.princess.shopapi.service.UserService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@RestController
@RequestMapping("/users")
class UserController(private val service: UserService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody details: UserDTO): UserDTO {
        log.info("Running POST /users method.")
        return service.create(details).also { log.info("User created.") }
    }

    @GetMapping("/{role}")
    fun findAll(@PathVariable role: Role): List<UserDTO> {
        log.info("Running GET /users/{role} method.")

        return service.findAll(role).also { log.info("Users fetched.") }
    }

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: UUID): UserDTO {
        log.info("Running GET /users/{id} method.")
        return service.find(id).also { log.info("User fetched.") }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @Valid @RequestBody details: UserDTO): UserDTO {
        log.info("Running PUT /users/{id} method.")
        return service.update(id, details).also { log.info("User updated.") }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: UUID) {
        log.info("Running DELETE /users/{id} method.")
        return service.delete(id).also { log.info("User deleted.") }
    }
}