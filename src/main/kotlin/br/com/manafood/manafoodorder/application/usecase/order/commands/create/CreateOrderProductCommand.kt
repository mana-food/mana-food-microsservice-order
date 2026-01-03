package br.com.manafood.manafoodorder.application.usecase.order.commands.create

import java.math.BigDecimal
import java.util.UUID

data class CreateOrderProductCommand(
    val productId: UUID,
    val quantity: BigDecimal
)
