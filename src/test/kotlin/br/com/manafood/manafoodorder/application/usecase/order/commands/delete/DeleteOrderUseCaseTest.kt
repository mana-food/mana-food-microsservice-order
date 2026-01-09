package br.com.manafood.manafoodorder.application.usecase.order.commands.delete

import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class DeleteOrderUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var deleteOrderUseCase: DeleteOrderUseCase

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        deleteOrderUseCase = DeleteOrderUseCase(orderRepository)
    }

    @Test
    fun `should delete order successfully`() {
        // Given
        val orderId = UUID.randomUUID()
        val deletedBy = UUID.randomUUID()
        val command = DeleteOrderCommand(id = orderId, deletedBy = deletedBy)

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

        val orderSlot = slot<Order>()
        every { orderRepository.findById(orderId) } returns existingOrder
        every { orderRepository.save(capture(orderSlot)) } returns existingOrder

        // When
        deleteOrderUseCase.execute(command)

        // Then
        verify(exactly = 1) { orderRepository.findById(orderId) }
        verify(exactly = 1) { orderRepository.save(any()) }
        assertTrue(orderSlot.captured.deleted)
        assertEquals(deletedBy, orderSlot.captured.updatedBy)
    }

    @Test
    fun `should throw exception when order not found`() {
        // Given
        val orderId = UUID.randomUUID()
        val command = DeleteOrderCommand(id = orderId, deletedBy = UUID.randomUUID())

        every { orderRepository.findById(orderId) } returns null

        // When & Then
        assertThrows<IllegalArgumentException> {
            deleteOrderUseCase.execute(command)
        }
        verify(exactly = 1) { orderRepository.findById(orderId) }
        verify(exactly = 0) { orderRepository.save(any()) }
    }
}

