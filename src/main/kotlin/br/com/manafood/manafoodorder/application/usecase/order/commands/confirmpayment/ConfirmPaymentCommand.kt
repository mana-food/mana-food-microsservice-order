package br.com.manafood.manafoodorder.application.usecase.order.commands.confirmpayment

import java.time.LocalDateTime
import java.util.UUID

data class ConfirmPaymentCommand(
    val orderId: UUID,
    val updatedBy: UUID,
    val updatedAt: LocalDateTime,
    val paymentStatus: String,
    val paymentId: String
)
