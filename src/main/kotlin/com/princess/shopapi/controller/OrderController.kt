package com.princess.shopapi.controller

import com.princess.shopapi.dto.OrderDTO
import com.princess.shopapi.dto.OrderStatus
import com.princess.shopapi.dto.OrderStatusDTO
import com.princess.shopapi.dto.QuantityDTO
import com.princess.shopapi.dto.Role
import com.princess.shopapi.service.OrderService
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
@RequestMapping("/orders")
class OrderController(private val service: OrderService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun create(@Valid @RequestBody details: OrderDTO, @AuthenticationPrincipal userId: UUID): OrderDTO {
        log.info("Running POST /orders method.")
        return service.create(userId, details).also { log.info("Order created.") }
    }

    @GetMapping
    fun findAll(@AuthenticationPrincipal userId: UUID, pageable: Pageable): Page<OrderDTO> {
        log.info("Running GET /orders method.")

        return service.findAll(userId, pageable).also { log.info("Orders fetched.") }
    }

    @GetMapping("/{id}")
    fun find(@PathVariable("id") id: UUID, @AuthenticationPrincipal userId: UUID): OrderDTO {
        log.info("Running GET /orders/{id} method.")
        return service.find(id, userId).also { log.info("Order fetched.") }
    }

    @PutMapping("/{id}/status")
    fun updateStatus(@PathVariable("id") id: UUID, @Valid @RequestBody status: OrderStatusDTO): OrderDTO {
        log.info("Running PUT /orders/{id} method.")
        return service.updateStatus(id, status.status).also { log.info("Order updated.") }
    }

    @PutMapping("/{id}/cancel")
    fun cancelOrder(@PathVariable("id") id: UUID): OrderDTO {
        log.info("Running PUT /orders/{id}/quantity method.")
        return service.cancelOrder(id)
            .also { log.info("Order quantity updated.") }
    }
}