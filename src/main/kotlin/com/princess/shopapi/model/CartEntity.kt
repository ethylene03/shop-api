package com.princess.shopapi.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "carts")
@EntityListeners(AuditingEntityListener::class)
class CartEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @OneToOne
    @JoinColumn(name = "user_id")
    var user: UserEntity? = null,

    @Column(nullable = false)
    var totalAmount: Double = 0.0,

    @OneToMany(mappedBy = "cart", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<CartItemEntity> = mutableListOf(),

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