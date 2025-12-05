package com.princess.shopapi.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID


@Entity
@Table(name = "order_items")
@EntityListeners(AuditingEntityListener::class)
class OrderItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    var product: ProductEntity? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: OrderEntity? = null,

    @Column(nullable = false)
    var quantity: Int = 1,

    @Column(nullable = false)
    var priceAtPurchase: Double = 0.0,

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