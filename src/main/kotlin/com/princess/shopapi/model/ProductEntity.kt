package com.princess.shopapi.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "products")
@EntityListeners(AuditingEntityListener::class)
class ProductEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(columnDefinition = "TEXT")
    var description: String = "",

    @Column(nullable = false)
    var price: Double = 0.0,

    @Column(nullable = false)
    var quantity: Int = 1,

    // Audit Columns
    @CreatedBy
    var createdBy: UUID? = null,
    @CreatedDate
    var createdAt: LocalDateTime? = null,
    @LastModifiedBy
    var modifiedBy: UUID? = null,
    @LastModifiedDate
    var modifiedAt: LocalDateTime? = null,
)