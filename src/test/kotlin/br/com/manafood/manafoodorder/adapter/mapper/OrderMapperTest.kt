package br.com.manafood.manafoodorder.adapter.mapper

import br.com.manafood.manafoodorder.adapter.request.commands.confirmpayment.ConfirmPaymentRequest
import br.com.manafood.manafoodorder.adapter.request.commands.create.CreateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.create.OrderProductRequest
import br.com.manafood.manafoodorder.adapter.request.commands.update.UpdateOrderRequest
import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderMapperTest {

    @Test
    fun `should map CreateOrderRequest to CreateOrderCommand`() {
        // Given
        val createdBy = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val productRequest = OrderProductRequest(productId = productId, quantity = 2)
        val request = CreateOrderRequest(
            paymentMethod = 0,
            products = listOf(productRequest)
        )

        // When
        val command = OrderMapper.toCreateCommand(request, createdBy)

        // Then
        assertEquals(createdBy, command.createdBy)
        assertEquals(0, command.paymentMethod)
        assertEquals(1, command.products.size)
        assertEquals(productId, command.products[0].productId)
        assertEquals(2, command.products[0].quantity)
    }

    @Test
    fun `should map UpdateOrderRequest to UpdateOrderCommand`() {
        // Given
        val orderId = UUID.randomUUID()
        val updatedBy = UUID.randomUUID()
        val updatedAt = LocalDateTime.now()
        val request = UpdateOrderRequest(
            id = orderId,
            orderStatus = 2
        )

        // When
        val command = OrderMapper.toUpdateCommand(request, updatedBy, updatedAt)

        // Then
        assertEquals(orderId, command.id)
        assertEquals(2, command.orderStatus)
        assertEquals(updatedBy, command.updatedBy)
    }

    @Test
    fun `should map to DeleteOrderCommand`() {
        // Given
        val orderId = UUID.randomUUID()
        val deletedBy = UUID.randomUUID()
        val deletedAt = LocalDateTime.now()

        // When
        val command = OrderMapper.toDeleteCommand(orderId, deletedBy, deletedAt)

        // Then
        assertEquals(orderId, command.id)
        assertEquals(deletedBy, command.deletedBy)
    }

    @Test
    fun `should map ConfirmPaymentRequest to ConfirmPaymentCommand`() {
        // Given
        val orderId = UUID.randomUUID()
        val confirmedBy = UUID.randomUUID()
        val deletedAt = LocalDateTime.now()
        val request = ConfirmPaymentRequest(
            orderId = orderId,
            paymentStatus = "approved",
            paymentId = "payment-123"
        )

        // When
        val command = OrderMapper.toConfirmPaymentCommand(request, confirmedBy, deletedAt)

        // Then
        assertEquals(orderId, command.orderId)
        assertEquals("approved", command.paymentStatus)
        assertEquals(confirmedBy, command.updatedBy)
    }

    @Test
    fun `should map Order to OrderResponse`() {
        // Given
        val orderId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val orderProduct = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = orderId,
            productId = productId,
            productName = "Test Product",
            unitPrice = BigDecimal("50.00"),
            quantity = 2
        )

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
            products = listOf(orderProduct)
        )

        // When
        val response = OrderMapper.toResponse(order)

        // Then
        assertEquals(orderId, response.id)
        assertEquals(1, response.orderStatus)
        assertEquals(BigDecimal("100.00"), response.totalAmount)
        assertEquals(0, response.paymentMethod)
        assertEquals(1, response.products.size)
        assertEquals(productId, response.products[0].productId)
        assertEquals(2, response.products[0].quantity)
    }

    @Test
    fun `should map Paged Order to Paged OrderResponse`() {
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

        val order2 = Order(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.RECEIVED,
            totalAmount = BigDecimal("200.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        val pagedOrders = Paged(
            items = listOf(order1, order2),
            page = 0,
            pageSize = 10,
            totalItems = 2,
            totalPages = 1
        )

        // When
        val result = OrderMapper.toResponsePaged(pagedOrders)

        // Then
        assertEquals(2, result.items.size)
        assertEquals(0, result.page)
        assertEquals(10, result.pageSize)
        assertEquals(2, result.totalItems)
        assertEquals(1, result.totalPages)
        assertEquals(1, result.items[0].orderStatus)
        assertEquals(2, result.items[1].orderStatus)
    }
}

