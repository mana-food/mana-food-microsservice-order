package br.com.manafood.manafoodorder.adapter.request.commands.create

import java.util.UUID

data class OrderProductRequest(
    val productId: UUID,
    val quantity: Int
)
