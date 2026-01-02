package br.com.manafood.manafoodorder.application.usecase.order.commands.create

import br.com.manafood.manafoodorder.application.dto.OrderProductRequest
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import java.util.UUID

data class CreateOrderCommand(
    val createdBy: UUID,
    val paymentMethod: PaymentMethod,
    val products: List<OrderProductRequest>
)
