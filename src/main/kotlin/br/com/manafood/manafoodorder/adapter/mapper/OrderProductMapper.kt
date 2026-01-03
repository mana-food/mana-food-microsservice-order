package br.com.manafood.manafoodorder.adapter.mapper

import br.com.manafood.manafoodorder.adapter.request.commands.create.OrderProductRequest
import br.com.manafood.manafoodorder.adapter.response.OrderProductResponse
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderProductCommand
import br.com.manafood.manafoodorder.domain.model.OrderProduct

object OrderProductMapper {
    fun toCreateCommandList(requestList: List<OrderProductRequest>) =
        requestList.map { request ->
            CreateOrderProductCommand(
                productId = request.productId,
                quantity = request.quantity
            )
        }

    fun toResponseList(orderProducts: List<OrderProduct>): List<OrderProductResponse> =
        orderProducts.map { orderProduct ->
            OrderProductResponse(
                productId = orderProduct.productId,
                quantity = orderProduct.quantity
            )
        }
}
