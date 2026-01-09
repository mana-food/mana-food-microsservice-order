package br.com.manafood.manafoodorder.application.usecase.order.queries.getreadyforkitchen

import br.com.manafood.manafoodorder.domain.common.Paged
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

class GetOrdersReadyForKitchenUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var getOrdersReadyForKitchenUseCase: GetOrdersReadyForKitchenUseCase

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        getOrdersReadyForKitchenUseCase = GetOrdersReadyForKitchenUseCase(orderRepository)
    }

    @Test
    fun `should return paged orders ready for kitchen`() {
        // Given
        val query = GetOrdersReadyForKitchenQuery(page = 0, pageSize = 10)

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

        every { orderRepository.findByOrdersReadyForKitchen(0, 10) } returns pagedOrders

        // When
        val result = getOrdersReadyForKitchenUseCase.execute(query)

        // Then
        assertNotNull(result)
        assertEquals(1, result.items.size)
        assertEquals(OrderStatus.RECEIVED, result.items[0].orderStatus)
        assertNotNull(result.items[0].orderConfirmationTime)
        verify(exactly = 1) { orderRepository.findByOrdersReadyForKitchen(0, 10) }
    }

    @Test
    fun `should return empty paged when no orders ready for kitchen`() {
        // Given
        val query = GetOrdersReadyForKitchenQuery(page = 0, pageSize = 10)

        val pagedOrders = Paged<Order>(
            items = emptyList(),
            page = 0,
            pageSize = 10,
            totalItems = 0,
            totalPages = 0
        )

        every { orderRepository.findByOrdersReadyForKitchen(0, 10) } returns pagedOrders

        // When
        val result = getOrdersReadyForKitchenUseCase.execute(query)

        // Then
        assertTrue(result.items.isEmpty())
        assertEquals(0, result.totalItems)
        verify(exactly = 1) { orderRepository.findByOrdersReadyForKitchen(0, 10) }
    }
}

