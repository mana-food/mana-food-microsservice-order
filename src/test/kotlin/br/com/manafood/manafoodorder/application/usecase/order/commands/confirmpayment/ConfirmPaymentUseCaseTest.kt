package br.com.manafood.manafoodorder.application.usecase.order.commands.confirmpayment

import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class ConfirmPaymentUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var confirmPaymentUseCase: ConfirmPaymentUseCase

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        confirmPaymentUseCase = ConfirmPaymentUseCase(orderRepository)
    }

    @Test
    fun `should confirm payment when approved`() {
        // Given
        val orderId = UUID.randomUUID()
        val command = ConfirmPaymentCommand(
            orderId = orderId,
            paymentStatus = "approved",
            updatedBy = UUID.randomUUID(),
            paymentId = "payment-123"
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
            products = emptyList()
        )

        val orderSlot = slot<Order>()
        every { orderRepository.findById(orderId) } returns order
        every { orderRepository.save(capture(orderSlot)) } answers { orderSlot.captured }

        // When
        confirmPaymentUseCase.execute(command)

        // Then
        verify(exactly = 1) { orderRepository.findById(orderId) }
        verify(exactly = 1) { orderRepository.save(any()) }
        assertEquals(OrderStatus.RECEIVED, orderSlot.captured.orderStatus)
        assertNotNull(orderSlot.captured.orderConfirmationTime)
    }

    @Test
    fun `should reject payment when not approved`() {
        // Given
        val orderId = UUID.randomUUID()
        val command = ConfirmPaymentCommand(
            orderId = orderId,
            paymentStatus = "rejected",
            updatedBy = UUID.randomUUID(),
            paymentId = "payment-456"
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
            products = emptyList()
        )

        val orderSlot = slot<Order>()
        every { orderRepository.findById(orderId) } returns order
        every { orderRepository.save(capture(orderSlot)) } answers { orderSlot.captured }

        // When
        confirmPaymentUseCase.execute(command)

        // Then
        verify(exactly = 1) { orderRepository.save(any()) }
        assertEquals(OrderStatus.REJECTED, orderSlot.captured.orderStatus)
        assertNull(orderSlot.captured.orderConfirmationTime)
    }

    @Test
    fun `should throw exception when order not found`() {
        // Given
        val orderId = UUID.randomUUID()
        val command = ConfirmPaymentCommand(
            orderId = orderId,
            paymentStatus = "approved",
            updatedBy = UUID.randomUUID(),
            paymentId = "payment-789"
        )

        every { orderRepository.findById(orderId) } returns null

        // When & Then
        assertThrows<IllegalArgumentException> {
            confirmPaymentUseCase.execute(command)
        }
        verify(exactly = 1) { orderRepository.findById(orderId) }
        verify(exactly = 0) { orderRepository.save(any()) }
    }
}

