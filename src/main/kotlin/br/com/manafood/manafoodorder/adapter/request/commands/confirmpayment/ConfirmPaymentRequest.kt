package br.com.manafood.manafoodorder.adapter.request.commands.confirmpayment

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class ConfirmPaymentRequest(
    @field:NotNull val orderId: UUID,
    @field:NotNull val paymentStatus: String,
    val paymentId: String?
)
