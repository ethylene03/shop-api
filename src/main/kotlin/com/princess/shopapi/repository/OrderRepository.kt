package com.princess.shopapi.repository

import com.princess.shopapi.model.OrderEntity
import com.princess.shopapi.model.ProductEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface OrderRepository : JpaRepository<OrderEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    fun findAllByBuyerId(userId: UUID, pageable: Pageable): Page<OrderEntity>
    fun findAllBySellerId(userId: UUID, pageable: Pageable): Page<OrderEntity>
}