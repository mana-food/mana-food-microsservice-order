package br.com.manafood.manafoodorder.application.usecase.order.commands.create

import java.util.UUID

data class CreateOrderProductCommand(
    val productId: UUID,
    val quantity: Int
)
