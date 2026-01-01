package br.com.manafood.manafoodorder.domain.model

import br.com.manafood.manafoodorder.domain.common.BaseEntity
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class Order(
    override val id: UUID,
    override val createdBy: UUID,
    override val createdAt: LocalDateTime = LocalDateTime.now(),
    override val updatedBy: UUID? = null,
    override val updatedAt: LocalDateTime? = null,
    override val deleted: Boolean = false,
    val orderConfirmationTime: LocalDateTime?,
    val orderStatus: OrderStatus,
    val totalAmount: BigDecimal,
    val paymentMethod: PaymentMethod,
    val products: List<OrderProduct>
) : BaseEntity(
    id = id,
    createdBy = createdBy,
    createdAt = createdAt,
    updatedBy = updatedBy,
    updatedAt = updatedAt,
    deleted = deleted
) {
    fun calculateTotal(): Order {
        return copy(totalAmount = products.sumOf { it.subtotal })
    }

    fun setStatus(status: OrderStatus): Order {
        return copy(
            orderStatus = status,
            updatedAt = updatedAt,
            orderConfirmationTime = if (status == OrderStatus.RECEIVED) LocalDateTime.now() else orderConfirmationTime
        )
    }
}