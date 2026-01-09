package br.com.manafood.manafoodorder.domain.model

import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderTest {

    @Test
    fun `should calculate total amount from products`() {
        // Given
        val orderId = UUID.randomUUID()
        val createdBy = UUID.randomUUID()
        val product1 = createOrderProduct(
            orderId = orderId,
            createdBy = createdBy,
            unitPrice = BigDecimal("10.00"),
            quantity = 2
        )
        val product2 = createOrderProduct(
            orderId = orderId,
            createdBy = createdBy,
            unitPrice = BigDecimal("15.00"),
            quantity = 3
        )

        val order = Order(
            id = orderId,
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.CREATED,
            totalAmount = BigDecimal.ZERO,
            paymentMethod = PaymentMethod.QR_CODE,
            products = listOf(product1, product2)
        )

        // When
        val orderWithTotal = order.calculateTotal()

        // Then
        assertEquals(0, BigDecimal("65.00").compareTo(orderWithTotal.totalAmount))
    }

    @Test
    fun `should set order status and confirmation time`() {
        // Given
        val orderId = UUID.randomUUID()
        val createdBy = UUID.randomUUID()
        val order = Order(
            id = orderId,
            createdBy = createdBy,
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

        // When
        val updatedOrder = order.setStatus(OrderStatus.RECEIVED)

        // Then
        assertEquals(OrderStatus.RECEIVED, updatedOrder.orderStatus)
        assertNotNull(updatedOrder.orderConfirmationTime)
    }

    @Test
    fun `should create order with correct properties`() {
        // Given
        val orderId = UUID.randomUUID()
        val createdBy = UUID.randomUUID()
        val createdAt = LocalDateTime.now()

        // When
        val order = Order(
            id = orderId,
            createdBy = createdBy,
            createdAt = createdAt,
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.CREATED,
            totalAmount = BigDecimal.ZERO,
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        // Then
        assertEquals(orderId, order.id)
        assertEquals(createdBy, order.createdBy)
        assertEquals(createdAt, order.createdAt)
        assertFalse(order.deleted)
        assertEquals(OrderStatus.CREATED, order.orderStatus)
        assertEquals(PaymentMethod.QR_CODE, order.paymentMethod)
    }

    private fun createOrderProduct(
        orderId: UUID,
        createdBy: UUID,
        unitPrice: BigDecimal,
        quantity: Int
    ): OrderProduct {
        return OrderProduct(
            id = UUID.randomUUID(),
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = orderId,
            productId = UUID.randomUUID(),
            productName = "Test Product",
            unitPrice = unitPrice,
            quantity = quantity
        )
    }
}

