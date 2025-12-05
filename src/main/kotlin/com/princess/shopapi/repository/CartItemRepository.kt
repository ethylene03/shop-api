package com.princess.shopapi.repository

import com.princess.shopapi.model.CartItemEntity
import com.princess.shopapi.model.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CartItemRepository : JpaRepository<CartItemEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    fun findByCartIdAndProductId(cartId: UUID?, productId: UUID?): CartItemEntity?
}