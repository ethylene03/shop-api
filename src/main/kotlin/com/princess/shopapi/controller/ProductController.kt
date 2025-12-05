package com.princess.shopapi.controller

import com.princess.shopapi.dto.ProductDTO
import com.princess.shopapi.dto.QuantityDTO
import com.princess.shopapi.dto.Role
import com.princess.shopapi.service.ProductService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@Validated
@RestController
@RequestMapping("/products")
class ProductController(private val service: ProductService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody details: ProductDTO): ProductDTO {
        log.info("Running POST /products method.")
        return service.create(details).also { log.info("Product created.") }
    }

    @GetMapping
    fun findAll(@RequestParam role: Role, @AuthenticationPrincipal userId: UUID, pageable: Pageable): Page<ProductDTO> {
        log.info("Running GET /products/{role} method.")

        return service.findAll(role, userId, pageable).also { log.info("Products fetched.") }
    }

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: UUID): ProductDTO {
        log.info("Running GET /products/{id} method.")
        return service.find(id).also { log.info("Product fetched.") }
    }

    @PutMapping("/{id}")
    fun update(@PathVariable("id") id: UUID, @Valid @RequestBody details: ProductDTO): ProductDTO {
        log.info("Running PUT /products/{id} method.")
        return service.update(id, details).also { log.info("Product updated.") }
    }

    @PutMapping("/{id}/quantity")
    fun addQuantity(@PathVariable("id") id: UUID, @RequestBody quantity: QuantityDTO): ProductDTO {
        log.info("Running PUT /products/{id}/quantity method.")
        return service.addQuantity(id, quantity.quantity)
            .also { log.info("Product quantity updated.") }
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    fun delete(@PathVariable("id") id: UUID) {
        log.info("Running DELETE /products/{id} method.")
        return service.delete(id).also { log.info("Product deleted.") }
    }
}