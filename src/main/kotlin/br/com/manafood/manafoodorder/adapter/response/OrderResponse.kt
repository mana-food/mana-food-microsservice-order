package br.com.manafood.manafoodorder.adapter.response

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class OrderResponse(
    val id: UUID,
    val orderStatus: Int,
    val totalAmount: BigDecimal,
    val paymentMethod: Int,
    val products: List<OrderProductResponse>,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)
