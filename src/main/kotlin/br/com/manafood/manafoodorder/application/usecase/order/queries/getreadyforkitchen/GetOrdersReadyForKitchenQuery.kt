package br.com.manafood.manafoodorder.application.usecase.order.queries.getreadyforkitchen

data class GetOrdersReadyForKitchenQuery(
    val page: Int = 0,
    val pageSize: Int = 10
)
