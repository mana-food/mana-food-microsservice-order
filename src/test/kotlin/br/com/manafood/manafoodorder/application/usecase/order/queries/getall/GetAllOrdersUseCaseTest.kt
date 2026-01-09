package br.com.manafood.manafoodorder.application.usecase.order.queries.getall

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

class GetAllOrdersUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var getAllOrdersUseCase: GetAllOrdersUseCase

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        getAllOrdersUseCase = GetAllOrdersUseCase(orderRepository)
    }

    @Test
    fun `should return paged orders`() {
        // Given
        val query = GetAllOrdersQuery(page = 0, pageSize = 10)

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

        every { orderRepository.findPaged(0, 10) } returns pagedOrders

        // When
        val result = getAllOrdersUseCase.execute(query)

        // Then
        assertNotNull(result)
        assertEquals(2, result.items.size)
        assertEquals(0, result.page)
        assertEquals(10, result.pageSize)
        assertEquals(2, result.totalItems)
        assertEquals(1, result.totalPages)
        verify(exactly = 1) { orderRepository.findPaged(0, 10) }
    }

    @Test
    fun `should return empty paged orders when no orders found`() {
        // Given
        val query = GetAllOrdersQuery(page = 0, pageSize = 10)

        val pagedOrders = Paged<Order>(
            items = emptyList(),
            page = 0,
            pageSize = 10,
            totalItems = 0,
            totalPages = 0
        )

        every { orderRepository.findPaged(0, 10) } returns pagedOrders

        // When
        val result = getAllOrdersUseCase.execute(query)

        // Then
        assertNotNull(result)
        assertTrue(result.items.isEmpty())
        assertEquals(0, result.totalItems)
        verify(exactly = 1) { orderRepository.findPaged(0, 10) }
    }

    @Test
    fun `should return second page of orders`() {
        // Given
        val query = GetAllOrdersQuery(page = 1, pageSize = 5)

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

        val pagedOrders = Paged(
            items = listOf(order),
            page = 1,
            pageSize = 5,
            totalItems = 6,
            totalPages = 2
        )

        every { orderRepository.findPaged(1, 5) } returns pagedOrders

        // When
        val result = getAllOrdersUseCase.execute(query)

        // Then
        assertEquals(1, result.page)
        assertEquals(5, result.pageSize)
        assertEquals(6, result.totalItems)
        assertEquals(2, result.totalPages)
    }
}

