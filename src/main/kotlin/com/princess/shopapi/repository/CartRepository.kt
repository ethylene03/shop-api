package com.princess.shopapi.repository

import com.princess.shopapi.model.CartEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CartRepository : JpaRepository<CartEntity, UUID> {
}