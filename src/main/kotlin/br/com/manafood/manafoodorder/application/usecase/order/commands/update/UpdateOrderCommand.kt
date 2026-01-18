package br.com.manafood.manafoodorder.application.usecase.order.commands.update

import java.time.LocalDateTime
import java.util.UUID

data class UpdateOrderCommand(
    val id: UUID,
    val updatedBy: UUID,
    val updatedAt: LocalDateTime,
    val orderStatus: Int
)
