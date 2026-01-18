package br.com.manafood.manafoodorder.application.usecase.order.commands.update

import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.dao.DataAccessException
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class UpdateOrderUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var updateOrderUseCase: UpdateOrderUseCase

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        updateOrderUseCase = UpdateOrderUseCase(orderRepository)
    }

    @Test
    fun `should update order status successfully`() {
        // Given
        val orderId = UUID.randomUUID()
        val updatedBy = UUID.randomUUID()
        val updatedAt = LocalDateTime.now()
        val command = UpdateOrderCommand(
            id = orderId,
            orderStatus = 2,
            updatedBy = updatedBy,
            updatedAt = updatedAt
        )

        val existingOrder = Order(
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

        val updatedOrder = existingOrder.copy(orderStatus = OrderStatus.RECEIVED)

        every { orderRepository.findById(orderId) } returns existingOrder
        every { orderRepository.save(any()) } returns updatedOrder

        // When
        val result = updateOrderUseCase.execute(command)

        // Then
        assertNotNull(result)
        assertEquals(OrderStatus.RECEIVED, result.orderStatus)
        verify(exactly = 1) { orderRepository.findById(orderId) }
        verify(exactly = 1) { orderRepository.save(any()) }
    }

    @Test
    fun `should throw exception when order not found`() {
        // Given
        val orderId = UUID.randomUUID()
        val updatedAt = LocalDateTime.now()
        val command = UpdateOrderCommand(
            id = orderId,
            orderStatus = 2,
            updatedBy = UUID.randomUUID(),
            updatedAt = updatedAt
        )

        every { orderRepository.findById(orderId) } returns null

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            updateOrderUseCase.execute(command)
        }
        assertTrue(exception.message!!.contains("Pedido com id $orderId não encontrado"))
        verify(exactly = 1) { orderRepository.findById(orderId) }
        verify(exactly = 0) { orderRepository.save(any()) }
    }

    @Test
    fun `should throw exception when repository save fails`() {
        // Given
        val orderId = UUID.randomUUID()
        val updatedAt = LocalDateTime.now()
        val command = UpdateOrderCommand(
            id = orderId,
            orderStatus = 3,
            updatedBy = UUID.randomUUID(),
            updatedAt = updatedAt
        )

        val existingOrder = Order(
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

        every { orderRepository.findById(orderId) } returns existingOrder
        every { orderRepository.save(any()) } throws mockk<DataAccessException>()

        // When & Then
        val exception = assertThrows<Exception> {
            updateOrderUseCase.execute(command)
        }
        assertTrue(exception.message!!.contains("Erro ao atualizar entidade"))
    }

    @Test
    fun `should update order to PREPARING status`() {
        // Given
        val orderId = UUID.randomUUID()
        val updatedAt = LocalDateTime.now()
        val command = UpdateOrderCommand(
            id = orderId,
            orderStatus = 3,
            updatedBy = UUID.randomUUID(),
            updatedAt = updatedAt
        )

        val existingOrder = Order(
            id = orderId,
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.RECEIVED,
            totalAmount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = emptyList()
        )

        val updatedOrder = existingOrder.copy(orderStatus = OrderStatus.PREPARING)

        every { orderRepository.findById(orderId) } returns existingOrder
        every { orderRepository.save(any()) } returns updatedOrder

        // When
        val result = updateOrderUseCase.execute(command)

        // Then
        assertEquals(OrderStatus.PREPARING, result.orderStatus)
    }
}

