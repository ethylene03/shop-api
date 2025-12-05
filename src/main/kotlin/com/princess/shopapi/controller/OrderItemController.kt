package com.princess.shopapi.controller

import com.princess.shopapi.dto.OrderItemDTO
import com.princess.shopapi.service.OrderItemService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@Validated
@RequestMapping("/order-items")
class OrderItemController(private val service: OrderItemService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping
    fun findAll(@AuthenticationPrincipal userId: UUID, pageable: Pageable): Page<OrderItemDTO> {
        log.info("Running GET /order-items method.")
        return service.findAll(userId, pageable).also { log.info("Order Items fetched.") }
    }
}