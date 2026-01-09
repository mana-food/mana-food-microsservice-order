package br.com.manafood.manafoodorder.adapter.controller

import br.com.manafood.manafoodorder.adapter.mapper.OrderMapper
import br.com.manafood.manafoodorder.adapter.request.commands.confirmpayment.ConfirmPaymentRequest
import br.com.manafood.manafoodorder.adapter.request.commands.create.CreateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.update.UpdateOrderRequest
import br.com.manafood.manafoodorder.adapter.response.OrderResponse
import br.com.manafood.manafoodorder.application.usecase.order.commands.confirmpayment.ConfirmPaymentUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.delete.DeleteOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.update.UpdateOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getall.GetAllOrdersQuery
import br.com.manafood.manafoodorder.application.usecase.order.queries.getall.GetAllOrdersUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getbyid.GetOrderByIdQuery
import br.com.manafood.manafoodorder.application.usecase.order.queries.getbyid.GetOrderByIdUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getreadyforkitchen.GetOrdersReadyForKitchenQuery
import br.com.manafood.manafoodorder.application.usecase.order.queries.getreadyforkitchen.GetOrdersReadyForKitchenUseCase
import br.com.manafood.manafoodorder.domain.common.Paged
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("api/order")
class OrderController(
    private val createOrderUseCase: CreateOrderUseCase,
    private val updateOrderUseCase: UpdateOrderUseCase,
    private val deleteOrderUseCase: DeleteOrderUseCase,
    private val getOrderByIdUseCase: GetOrderByIdUseCase,
    private val getAllOrdersUseCase: GetAllOrdersUseCase,
    private val getOrdersReadyForKitchenUseCase: GetOrdersReadyForKitchenUseCase,
    private val confirmPaymentUseCase: ConfirmPaymentUseCase
) {

    @PostMapping
    fun create(
        @RequestBody request: CreateOrderRequest
    ): ResponseEntity<OrderResponse> {
        val createdBy = UUID.randomUUID()
        val command = OrderMapper.toCreateCommand(request, createdBy)
        val order = createOrderUseCase.execute(command)

        return ResponseEntity.ok(OrderMapper.toResponse(order))
    }

    @PutMapping
    fun update(
        @RequestBody request: UpdateOrderRequest
    ): ResponseEntity<OrderResponse> {
        val updatedBy = UUID.randomUUID()
        val command = OrderMapper.toUpdateCommand(request, updatedBy)
        val order = updateOrderUseCase.execute(command)

        return ResponseEntity.ok(OrderMapper.toResponse(order))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        val deletedBy = UUID.randomUUID()
        val command = OrderMapper.toDeleteCommand(id, deletedBy)
        deleteOrderUseCase.execute(command)

        return ResponseEntity.noContent().build()
    }

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<OrderResponse> {
        val order = getOrderByIdUseCase.execute(GetOrderByIdQuery(id))
        return if (order != null) ResponseEntity.ok(OrderMapper.toResponse(order)) else ResponseEntity.notFound()
            .build()
    }

    @GetMapping
    fun getAll(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<Paged<OrderResponse>> {
        val orders = getAllOrdersUseCase.execute(GetAllOrdersQuery(page, pageSize))
        return ResponseEntity.ok(OrderMapper.toResponsePaged(orders))
    }

    @GetMapping("/ready")
    fun getOrdersReadyForKitchen(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") pageSize: Int
    ): ResponseEntity<Paged<OrderResponse>> {
        val orders = getOrdersReadyForKitchenUseCase.execute(GetOrdersReadyForKitchenQuery(page, pageSize))
        return ResponseEntity.ok(OrderMapper.toResponsePaged(orders))
    }

    @PostMapping("/confirm-payment")
    fun confirmPayment(
        @RequestBody request: ConfirmPaymentRequest
    ): ResponseEntity<Void> {
        val createdBy = UUID.randomUUID()
        val command = OrderMapper.toConfirmPaymentCommand(request, createdBy)
        confirmPaymentUseCase.execute(command)

        return ResponseEntity.noContent().build()
    }
}