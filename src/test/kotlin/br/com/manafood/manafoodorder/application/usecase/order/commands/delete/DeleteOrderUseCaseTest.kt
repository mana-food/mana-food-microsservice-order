package br.com.manafood.manafoodorder.application.usecase.order.commands.delete

import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
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

        every { orderRepository.deleteById(orderId) } returns true

        // When
        deleteOrderUseCase.execute(command)

        // Then
        verify(exactly = 1) { orderRepository.deleteById(orderId) }
    }

    @Test
    fun `should throw exception when order not found`() {
        // Given
        val orderId = UUID.randomUUID()
        val command = DeleteOrderCommand(id = orderId, deletedBy = UUID.randomUUID())

        every { orderRepository.deleteById(orderId) } returns false

        // When & Then
        assertThrows<IllegalArgumentException> {
            deleteOrderUseCase.execute(command)
        }
        verify(exactly = 1) { orderRepository.deleteById(orderId) }
    }
}

