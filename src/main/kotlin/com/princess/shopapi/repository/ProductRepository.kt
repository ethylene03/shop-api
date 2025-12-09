package com.princess.shopapi.repository

import com.princess.shopapi.model.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface ProductRepository : JpaRepository<ProductEntity, UUID>, JpaSpecificationExecutor<ProductEntity> {
    fun findByIdAndIsDeletedFalse(id: UUID): Optional<ProductEntity>
}