package br.com.manafood.manafoodorder.adapter.request.commands.create

import jakarta.validation.constraints.NotNull

data class CreateOrderRequest(
    @field:NotNull val paymentMethod: Int,
    @field:NotNull val products: List<OrderProductRequest>
)
