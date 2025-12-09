package com.princess.shopapi.controller

import com.princess.shopapi.dto.CartDTO
import com.princess.shopapi.service.CartService
import org.slf4j.LoggerFactory
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/carts")
class CartController(private val service: CartService) {
    private val log = LoggerFactory.getLogger(this::class.java)

    @GetMapping
    fun find(@AuthenticationPrincipal userId: UUID): CartDTO {
        log.info("Running GET /carts method.")

        return service.find(userId)
            .also { log.info("Fetched.") }
    }
}