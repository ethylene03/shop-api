package com.princess.shopapi.repository

import com.princess.shopapi.model.OrderItemEntity
import com.princess.shopapi.model.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderItemRepository : JpaRepository<OrderItemEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    fun findAllByOrderUserId(userId: UUID, pageable: Pageable): Page<OrderItemEntity>
    fun findAllByProductCreatedBy(userId: UUID, pageable: Pageable): Page<OrderItemEntity>
}