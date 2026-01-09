package br.com.manafood.manafoodorder.application.usecase.order.queries.getall

data class GetAllOrdersQuery(
    val page: Int = 0,
    val pageSize: Int = 10
)
