package br.com.manafood.manafoodorder.infrastructure.persistence.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "order")
class OrderJpaEntity(

    id: UUID,

    @Column(name = "order_confirmation_time")
    val orderConfirmationTime: LocalDateTime? = null,

    @Column(nullable = false, name = "order_status")
    val orderStatus: Int,

    @Column(nullable = false, name = "total_amount")
    val totalAmount: BigDecimal,

    @Column(nullable = false, name = "payment_method")
    val paymentMethod: Int,

    @OneToMany(
        mappedBy = "order",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    var products: MutableList<OrderProductJpaEntity> = mutableListOf(),

    createdAt: LocalDateTime,
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
