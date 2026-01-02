package br.com.manafood.manafoodorder.application.usecase.order.commands.delete

import java.util.UUID

data class DeleteOrderCommand(
    val id: UUID,
    val deletedBy: UUID
)
