package com.princess.shopapi.controller

import com.princess.shopapi.dto.CartItemDTO
import com.princess.shopapi.service.CartItemService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@RestController
@RequestMapping("/cart-items")
class CartItemController(private val service: CartItemService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody details: CartItemDTO): CartItemDTO {
        log.info("Running POST /cart-items method.")
        return service.create(details).also { log.info("CartItem created.") }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @Valid @RequestBody details: CartItemDTO): CartItemDTO {
        log.info("Running PUT /cart-items/{id} method.")
        return service.update(id, details).also { log.info("CartItem updated.") }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: UUID) {
        log.info("Running DELETE /cart-items/{id} method.")
        return service.delete(id).also { log.info("CartItem deleted.") }
    }
}