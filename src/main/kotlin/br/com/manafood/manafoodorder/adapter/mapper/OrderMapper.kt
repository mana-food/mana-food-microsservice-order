package br.com.manafood.manafoodorder.adapter.mapper

import br.com.manafood.manafoodorder.adapter.request.commands.confirmpayment.ConfirmPaymentRequest
import br.com.manafood.manafoodorder.adapter.request.commands.create.CreateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.update.UpdateOrderRequest
import br.com.manafood.manafoodorder.adapter.response.OrderResponse
import br.com.manafood.manafoodorder.application.usecase.order.commands.confirmpayment.ConfirmPaymentCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.delete.DeleteOrderCommand
import br.com.manafood.manafoodorder.application.usecase.order.commands.update.UpdateOrderCommand
import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import java.time.LocalDateTime
import java.util.UUID
import kotlin.collections.map

object OrderMapper {
    fun toCreateCommand(request: CreateOrderRequest, createdBy: UUID) =
        CreateOrderCommand(
            createdBy = createdBy,
            paymentMethod = request.paymentMethod,
            products = OrderProductMapper.toCreateCommandList(request.products)
        )

    fun toUpdateCommand(request: UpdateOrderRequest, updatedBy: UUID, updatedAt: LocalDateTime) =
        UpdateOrderCommand(
            id = request.id,
            orderStatus = request.orderStatus,
            updatedBy = updatedBy,
            updatedAt = updatedAt
        )

    fun toDeleteCommand(id: UUID, deletedBy: UUID, deletedAt: LocalDateTime) =
        DeleteOrderCommand(
            id = id,
            deletedBy = deletedBy,
            deletedAt = deletedAt
        )

    fun toConfirmPaymentCommand(request: ConfirmPaymentRequest, confirmedBy: UUID, createdAt: LocalDateTime) =
        ConfirmPaymentCommand(
            orderId = request.orderId,
            paymentStatus = request.paymentStatus,
            paymentId = request.paymentStatus,
            updatedBy = confirmedBy,
            updatedAt = createdAt
        )

    fun toResponse(order: Order): OrderResponse =
        OrderResponse(
            id = order.id,
            orderStatus = order.orderStatus.code,
            totalAmount = order.totalAmount,
            paymentMethod = order.paymentMethod.code,
            products = OrderProductMapper.toResponseList(order.products),
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
