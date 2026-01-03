package br.com.manafood.manafoodorder.adapter.mapper

import br.com.manafood.manafoodorder.adapter.request.commands.create.CreateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.update.UpdateOrderRequest
import br.com.manafood.manafoodorder.adapter.response.OrderResponse
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.delete.DeleteOrderCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.update.UpdateOrderCommand
import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import java.util.UUID
import kotlin.collections.map

object OrderMapper {
    // TODO: Finalizar implementação dos mapeamentos
    fun toCreateCommand(request: CreateOrderRequest, createdBy: UUID) =
        CreateOrderCommand(
            createdBy = createdBy,
            paymentMethod = request.paymentMethod,
            products = OrderProductMapper.toCreateCommand(request.products)
        )

    fun toUpdateCommand(request: UpdateOrderRequest, updatedBy: UUID) =
        UpdateOrderCommand(
            id = request.id,
            updatedBy = updatedBy
        )

    fun toDeleteCommand(id: UUID, deletedBy: UUID) =
        DeleteOrderCommand(
            id = id,
            deletedBy = deletedBy
        )

    fun toResponse(order: Order): OrderResponse =
        OrderResponse(
            id = order.id,
            orderStatus = order.orderStatus.code,
            totalAmount = order.totalAmount,
            paymentMethod = order.paymentMethod.code,
            products = OrderProductMapper.toResponse(order.products),
            createdAt = order.createdAt,
            updatedAt = order.updatedAt
        )

    fun toResponsePaged(orderPaged: Paged<Order>): Paged<OrderResponse> {
        val orderResponses = orderPaged.items.map { toResponse(it) }
        return Paged(
            items = orderResponses,
            page = orderPaged.page,
            pageSize = orderPaged.pageSize,
            totalItems = orderPaged.totalItems,
            totalPages = orderPaged.totalPages
        )
    }
}
