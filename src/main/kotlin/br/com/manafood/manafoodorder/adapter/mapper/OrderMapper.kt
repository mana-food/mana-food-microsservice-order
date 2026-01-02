package br.com.manafood.manafoodorder.adapter.mapper

import br.com.manafood.manafoodorder.adapter.request.commands.create.CreateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.update.UpdateOrderRequest
import br.com.manafood.manafoodorder.adapter.response.OrderResponse
import br.com.manafood.manafoodorder.application.dto.CreateOrderCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.delete.DeleteOrderCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.update.UpdateOrderCommand
import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import java.util.UUID
import kotlin.collections.map

object OrderMapper {

    fun toCreateCommand(request: CreateOrderRequest, createdBy: UUID) =
        CreateOrderCommand(
            createdBy = createdBy,

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
            orderStatus = TODO(),
            totalAmount = TODO(),
            paymentMethod = TODO(),
            products = TODO(),
            createdAt = order.createdAt,
            updatedAt = order.updatedAt
        )

    fun toResponsePaged(catehoriesPaged: Paged<Order>): Paged<OrderResponse> {
        val orderResponses = catehoriesPaged.items.map { toResponse(it) }
        return Paged(
            items = orderResponses,
            page = catehoriesPaged.page,
            pageSize = catehoriesPaged.pageSize,
            totalItems = catehoriesPaged.totalItems,
            totalPages = catehoriesPaged.totalPages
        )
    }
}
