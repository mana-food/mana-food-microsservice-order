package br.com.manafood.manafoodorder.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "order_product")
class OrderProductJpaEntity(
    id: UUID,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    val order: OrderJpaEntity,

    @Column(nullable = false, name = "product_id")
    val productId: UUID,

    @Column(nullable = false, name = "product_name")
    val productName: String,

    @Column(nullable = false, name = "unit_price")
    val unitPrice: BigDecimal,

    @Column(nullable = false, name = "quantity")
    val quantity: BigDecimal,

    @Column(nullable = false, name = "subtotal")
    val subtotal: BigDecimal,

    createdAt: LocalDateTime = LocalDateTime.now(),
    createdBy: UUID,
    updatedAt: LocalDateTime? = null,
    updatedBy: UUID? = null,
    deleted: Boolean = false
) : BaseJpaEntity(
    id = id,
    createdAt = createdAt,
    createdBy = createdBy,
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deleted = deleted
)
