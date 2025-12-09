package com.princess.shopapi.model

import com.princess.shopapi.dto.Role
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener::class)
class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var username: String = "",

    @Column(nullable = false)
    var password: String = "",

    @Enumerated(EnumType.STRING)
    var role: Role = Role.BUYER,

    @OneToOne(mappedBy = "user", cascade = [CascadeType.ALL])
    var cart: CartEntity? = null,

    @CreatedDate
    var createdDate: LocalDateTime? = null
)