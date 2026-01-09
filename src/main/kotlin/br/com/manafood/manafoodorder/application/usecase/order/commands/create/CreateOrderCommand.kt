package br.com.manafood.manafoodorder.application.usecase.order.commands.create

import java.util.UUID

data class CreateOrderCommand(
    val createdBy: UUID,
    val paymentMethod: Int,
    val products: List<CreateOrderProductCommand>
)
