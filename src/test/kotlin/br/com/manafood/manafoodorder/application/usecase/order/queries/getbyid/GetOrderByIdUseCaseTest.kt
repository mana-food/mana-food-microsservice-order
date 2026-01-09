package br.com.manafood.manafoodorder.application.usecase.order.queries.getbyid

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
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class GetOrderByIdUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var getOrderByIdUseCase: GetOrderByIdUseCase

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        getOrderByIdUseCase = GetOrderByIdUseCase(orderRepository)
    }

    @Test
    fun `should return order when found`() {
        // Given
        val orderId = UUID.randomUUID()
        val query = GetOrderByIdQuery(id = orderId)

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

        every { orderRepository.findById(orderId) } returns order

        // When
        val result = getOrderByIdUseCase.execute(query)

        // Then
        assertNotNull(result)
        assertEquals(orderId, result?.id)
        assertEquals(OrderStatus.CREATED, result?.orderStatus)
        verify(exactly = 1) { orderRepository.findById(orderId) }
    }

    @Test
    fun `should return null when order not found`() {
        // Given
        val orderId = UUID.randomUUID()
        val query = GetOrderByIdQuery(id = orderId)

        every { orderRepository.findById(orderId) } returns null

        // When
        val result = getOrderByIdUseCase.execute(query)

        // Then
        assertNull(result)
        verify(exactly = 1) { orderRepository.findById(orderId) }
    }
}

