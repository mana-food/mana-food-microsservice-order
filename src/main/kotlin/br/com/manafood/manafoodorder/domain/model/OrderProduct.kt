package br.com.manafood.manafoodorder.domain.model

import br.com.manafood.manafoodorder.domain.common.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class OrderProduct(
    override val id: UUID,
    override val createdBy: UUID,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override val updatedBy: UUID? = null,
    override val updatedAt: LocalDateTime? = null,
    override val deleted: Boolean = false,
    val orderId: UUID,
    val productId: UUID, 
    val productName: String,  
    val unitPrice: BigDecimal,
    val quantity: Int,
    val subtotal: BigDecimal = unitPrice * quantity.toDouble().toBigDecimal()
) : BaseEntity(
    id = id,
    createdBy = createdBy,
    createdAt = createdAt,
    updatedBy = updatedBy,
    updatedAt = updatedAt,
    deleted = deleted
)