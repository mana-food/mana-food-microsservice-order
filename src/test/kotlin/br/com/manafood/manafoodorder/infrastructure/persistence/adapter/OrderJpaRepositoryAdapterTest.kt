package br.com.manafood.manafoodorder.infrastructure.persistence.adapter

import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import br.com.manafood.manafoodorder.infrastructure.persistence.entity.OrderJpaEntity
import br.com.manafood.manafoodorder.infrastructure.persistence.repository.OrderJpaRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

class OrderJpaRepositoryAdapterTest {

    private lateinit var springDataRepository: OrderJpaRepository
    private lateinit var orderJpaRepositoryAdapter: OrderJpaRepositoryAdapter

    @BeforeEach
    fun setUp() {
        springDataRepository = mockk()
        orderJpaRepositoryAdapter = OrderJpaRepositoryAdapter(springDataRepository)
    }

    @Test
    fun `should save order successfully`() {
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

        val orderJpaSlot = slot<OrderJpaEntity>()
        every { springDataRepository.save(capture(orderJpaSlot)) } answers { orderJpaSlot.captured }

        // When
        val result = orderJpaRepositoryAdapter.save(order)

        // Then
        assertNotNull(result)
        assertEquals(orderId, result.id)
        verify(exactly = 1) { springDataRepository.save(any()) }
    }

    @Test
    fun `should find order by id successfully`() {
        // Given
        val orderId = UUID.randomUUID()
        val orderJpa = OrderJpaEntity(
            id = orderId,
            orderConfirmationTime = null,
            paymentMethod = 0,
            orderStatus = 1,
            totalAmount = BigDecimal("100.00"),
            products = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            createdBy = UUID.randomUUID(),
            updatedBy = null,
            deleted = false
        )

        every { springDataRepository.findByIdAndNotDeleted(orderId) } returns Optional.of(orderJpa)

        // When
        val result = orderJpaRepositoryAdapter.findById(orderId)

        // Then
        assertNotNull(result)
        assertEquals(orderId, result?.id)
        verify(exactly = 1) { springDataRepository.findByIdAndNotDeleted(orderId) }
    }

    @Test
    fun `should return null when order not found by id`() {
        // Given
        val orderId = UUID.randomUUID()

        every { springDataRepository.findByIdAndNotDeleted(orderId) } returns Optional.empty()

        // When
        val result = orderJpaRepositoryAdapter.findById(orderId)

        // Then
        assertNull(result)
        verify(exactly = 1) { springDataRepository.findByIdAndNotDeleted(orderId) }
    }

    @Test
    fun `should find orders by ids`() {
        // Given
        val orderId1 = UUID.randomUUID()
        val orderId2 = UUID.randomUUID()
        val ids = listOf(orderId1, orderId2)

        val orderJpa1 = OrderJpaEntity(
            id = orderId1,
            orderConfirmationTime = null,
            paymentMethod = 0,
            orderStatus = 1,
            totalAmount = BigDecimal("100.00"),
            products = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            createdBy = UUID.randomUUID(),
            updatedBy = null,
            deleted = false
        )

        val orderJpa2 = OrderJpaEntity(
            id = orderId2,
            orderConfirmationTime = null,
            paymentMethod = 0,
            orderStatus = 2,
            totalAmount = BigDecimal("200.00"),
            products = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            createdBy = UUID.randomUUID(),
            updatedBy = null,
            deleted = false
        )

        every { springDataRepository.findAllByIdAndNotDeleted(ids) } returns listOf(orderJpa1, orderJpa2)

        // When
        val result = orderJpaRepositoryAdapter.findByIds(ids)

        // Then
        assertEquals(2, result.size)
        assertEquals(orderId1, result[0].id)
        assertEquals(orderId2, result[1].id)
        verify(exactly = 1) { springDataRepository.findAllByIdAndNotDeleted(ids) }
    }

    @Test
    fun `should find paged orders`() {
        // Given
        val orderJpa = OrderJpaEntity(
            id = UUID.randomUUID(),
            orderConfirmationTime = null,
            paymentMethod = 0,
            orderStatus = 1,
            totalAmount = BigDecimal("100.00"),
            products = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            createdBy = UUID.randomUUID(),
            updatedBy = null,
            deleted = false
        )

        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(orderJpa), pageable, 1)

        val pageableSlot = slot<Pageable>()
        every { springDataRepository.findPaged(capture(pageableSlot)) } returns page

        // When
        val result = orderJpaRepositoryAdapter.findPaged(0, 10)

        // Then
        assertNotNull(result)
        assertEquals(1, result.items.size)
        assertEquals(0, result.page)
        assertEquals(10, result.pageSize)
        verify(exactly = 1) { springDataRepository.findPaged(any()) }
    }

    @Test
    fun `should find orders ready for kitchen`() {
        // Given
        val orderJpa = OrderJpaEntity(
            id = UUID.randomUUID(),
            orderConfirmationTime = LocalDateTime.now(),
            paymentMethod = 0,
            orderStatus = 2,
            totalAmount = BigDecimal("100.00"),
            products = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            createdBy = UUID.randomUUID(),
            updatedBy = null,
            deleted = false
        )

        val pageable = PageRequest.of(0, 10)
        val page = PageImpl(listOf(orderJpa), pageable, 1)

        every { springDataRepository.findByOrdersReadyForKitchenPaged(any()) } returns page

        // When
        val result = orderJpaRepositoryAdapter.findByOrdersReadyForKitchen(0, 10)

        // Then
        assertNotNull(result)
        assertEquals(1, result.items.size)
        assertEquals(OrderStatus.RECEIVED, result.items[0].orderStatus)
        verify(exactly = 1) { springDataRepository.findByOrdersReadyForKitchenPaged(any()) }
    }

    @Test
    fun `should delete order by id`() {
        // Given
        val orderId = UUID.randomUUID()

        every { springDataRepository.deleteById(orderId) } returns Unit
        every { springDataRepository.existsByIdAndNotDeleted(orderId) } returns false

        // When
        val result = orderJpaRepositoryAdapter.deleteById(orderId)

        // Then
        assertTrue(result)
        verify(exactly = 1) { springDataRepository.deleteById(orderId) }
        verify(exactly = 1) { springDataRepository.existsByIdAndNotDeleted(orderId) }
    }
}

