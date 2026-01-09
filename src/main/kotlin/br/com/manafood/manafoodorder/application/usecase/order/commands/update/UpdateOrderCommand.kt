package br.com.manafood.manafoodorder.application.usecase.order.commands.update

import java.util.UUID

data class UpdateOrderCommand(
    val id: UUID,
    val updatedBy: UUID,
    val orderStatus: Int
)
