package br.com.manafood.manafoodorder.adapter.controller

import br.com.manafood.manafoodorder.adapter.request.commands.confirmpayment.ConfirmPaymentRequest
import br.com.manafood.manafoodorder.adapter.request.commands.create.CreateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.create.OrderProductRequest
import br.com.manafood.manafoodorder.adapter.request.commands.update.UpdateOrderRequest
import br.com.manafood.manafoodorder.application.usecase.order.commands.confirmpayment.ConfirmPaymentUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.delete.DeleteOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.update.UpdateOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getall.GetAllOrdersUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getbyid.GetOrderByIdUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getreadyforkitchen.GetOrdersReadyForKitchenUseCase
import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderControllerTest {

    private lateinit var createOrderUseCase: CreateOrderUseCase
    private lateinit var updateOrderUseCase: UpdateOrderUseCase
    private lateinit var deleteOrderUseCase: DeleteOrderUseCase
    private lateinit var getOrderByIdUseCase: GetOrderByIdUseCase
    private lateinit var getAllOrdersUseCase: GetAllOrdersUseCase
    private lateinit var getOrdersReadyForKitchenUseCase: GetOrdersReadyForKitchenUseCase
    private lateinit var confirmPaymentUseCase: ConfirmPaymentUseCase
    private lateinit var orderController: OrderController

    @BeforeEach
    fun setUp() {
        createOrderUseCase = mockk()
        updateOrderUseCase = mockk()
        deleteOrderUseCase = mockk()
        getOrderByIdUseCase = mockk()
        getAllOrdersUseCase = mockk()
        getOrdersReadyForKitchenUseCase = mockk()
        confirmPaymentUseCase = mockk()

        orderController = OrderController(
            createOrderUseCase,
            updateOrderUseCase,
            deleteOrderUseCase,
            getOrderByIdUseCase,
            getAllOrdersUseCase,
            getOrdersReadyForKitchenUseCase,
            confirmPaymentUseCase
        )
    }

    @Test
    fun `should create order successfully`() {
        // Given
        val productId = UUID.randomUUID()
        val productRequest = OrderProductRequest(productId = productId, quantity = 2)
        val request = CreateOrderRequest(paymentMethod = 0, products = listOf(productRequest))

        val order = Order(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.CREATED,
            totalAmount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        every { createOrderUseCase.execute(any()) } returns order

        // When
        val response = orderController.create(request)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(order.id, response.body?.id)
        assertEquals(1, response.body?.orderStatus)
        verify(exactly = 1) { createOrderUseCase.execute(any()) }
    }

    @Test
    fun `should update order successfully`() {
        // Given
        val orderId = UUID.randomUUID()
        val request = UpdateOrderRequest(id = orderId, orderStatus = 2)

        val order = Order(
            id = orderId,
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = UUID.randomUUID(),
            updatedAt = LocalDateTime.now(),
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.RECEIVED,
            totalAmount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        every { updateOrderUseCase.execute(any()) } returns order

        // When
        val response = orderController.update(request)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(orderId, response.body?.id)
        assertEquals(2, response.body?.orderStatus)
        verify(exactly = 1) { updateOrderUseCase.execute(any()) }
    }

    @Test
    fun `should delete order successfully`() {
        // Given
        val orderId = UUID.randomUUID()

        justRun { deleteOrderUseCase.execute(any()) }

        // When
        val response = orderController.delete(orderId)

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { deleteOrderUseCase.execute(any()) }
    }

    @Test
    fun `should get order by id successfully`() {
        // Given
        val orderId = UUID.randomUUID()
        val order = Order(
            id = orderId,
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.CREATED,
            totalAmount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        every { getOrderByIdUseCase.execute(any()) } returns order

        // When
        val response = orderController.getById(orderId)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(orderId, response.body?.id)
        verify(exactly = 1) { getOrderByIdUseCase.execute(any()) }
    }

    @Test
    fun `should return not found when order does not exist`() {
        // Given
        val orderId = UUID.randomUUID()

        every { getOrderByIdUseCase.execute(any()) } returns null

        // When
        val response = orderController.getById(orderId)

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
        assertNull(response.body)
        verify(exactly = 1) { getOrderByIdUseCase.execute(any()) }
    }

    @Test
    fun `should get all orders successfully`() {
        // Given
        val order1 = Order(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.CREATED,
            totalAmount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        val pagedOrders = Paged(
            items = listOf(order1),
            page = 0,
            pageSize = 10,
            totalItems = 1,
            totalPages = 1
        )

        every { getAllOrdersUseCase.execute(any()) } returns pagedOrders

        // When
        val response = orderController.getAll(0, 10)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.items?.size)
        assertEquals(0, response.body?.page)
        verify(exactly = 1) { getAllOrdersUseCase.execute(any()) }
    }

    @Test
    fun `should get orders ready for kitchen successfully`() {
        // Given
        val order = Order(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = LocalDateTime.now(),
            orderStatus = OrderStatus.RECEIVED,
            totalAmount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        val pagedOrders = Paged(
            items = listOf(order),
            page = 0,
            pageSize = 10,
            totalItems = 1,
            totalPages = 1
        )

        every { getOrdersReadyForKitchenUseCase.execute(any()) } returns pagedOrders

        // When
        val response = orderController.getOrdersReadyForKitchen(0, 10)

        // Then
        assertEquals(HttpStatus.OK, response.statusCode)
        assertNotNull(response.body)
        assertEquals(1, response.body?.items?.size)
        verify(exactly = 1) { getOrdersReadyForKitchenUseCase.execute(any()) }
    }

    @Test
    fun `should confirm payment successfully`() {
        // Given
        val orderId = UUID.randomUUID()
        val request = ConfirmPaymentRequest(
            orderId = orderId,
            paymentStatus = "approved",
            paymentId = "payment-123"
        )

        justRun { confirmPaymentUseCase.execute(any()) }

        // When
        val response = orderController.confirmPayment(request)

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
        verify(exactly = 1) { confirmPaymentUseCase.execute(any()) }
    }
}

