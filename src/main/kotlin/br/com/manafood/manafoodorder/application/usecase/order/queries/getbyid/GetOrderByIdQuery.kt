package br.com.manafood.manafoodorder.application.usecase.order.queries.getbyid

import java.util.UUID

data class GetOrderByIdQuery(
    val id: UUID
)
