package com.princess.shopapi.service

import com.princess.shopapi.dto.OrderItemDTO
import com.princess.shopapi.dto.Role
import com.princess.shopapi.helpers.ResourceNotFoundException
import com.princess.shopapi.helpers.toOrderItemResponse
import com.princess.shopapi.model.OrderItemEntity
import com.princess.shopapi.repository.OrderItemRepository
import com.princess.shopapi.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OrderItemService(private val repository: OrderItemRepository, private val userRepository: UserRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun findAll(userId: UUID, pageable: Pageable): Page<OrderItemDTO> {
        log.debug("Finding user..")
        val user = userRepository.findById(userId).orElseThrow {
            log.error("User does not exist.")
            ResourceNotFoundException("User does not exist.")
        }

        val page = when (user.role) {
            Role.BUYER -> repository.findAllByOrderUserId(userId, pageable)
            Role.SELLER -> repository.findAllByProductCreatedBy(userId, pageable)
        }

        val list = page.content.map { it.toOrderItemResponse() }
        return PageImpl(
            list,
            page.pageable,
            page.totalElements
        )
    }
}