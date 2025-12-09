package com.princess.shopapi.service

import com.princess.shopapi.dto.CartDTO
import com.princess.shopapi.helpers.ResourceNotFoundException
import com.princess.shopapi.helpers.toCartResponse
import com.princess.shopapi.repository.CartRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CartService(private val repository: CartRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun find(userId: UUID): CartDTO {
        log.debug("Finding cart by user..")
        return repository.findByUserId(userId).orElseThrow {
            log.error("Cart does not exist.")
            ResourceNotFoundException("Cart does not exist.")
        }.toCartResponse()
    }
}