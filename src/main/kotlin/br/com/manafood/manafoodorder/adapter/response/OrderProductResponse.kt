package br.com.manafood.manafoodorder.adapter.response

import java.math.BigDecimal
import java.util.UUID

data class OrderProductResponse(
    val productId: UUID,
    val quantity: Int
)
