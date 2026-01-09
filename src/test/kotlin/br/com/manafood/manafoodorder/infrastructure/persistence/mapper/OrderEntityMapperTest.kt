package br.com.manafood.manafoodorder.infrastructure.persistence.mapper

import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import br.com.manafood.manafoodorder.infrastructure.persistence.entity.OrderJpaEntity
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderEntityMapperTest {

    @Test
    fun `should map Order domain to OrderJpaEntity`() {
        // Given
        val orderId = UUID.randomUUID()
        val createdBy = UUID.randomUUID()
        val orderProduct = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = orderId,
            productId = UUID.randomUUID(),
            productName = "Test Product",
            unitPrice = BigDecimal("50.00"),
            quantity = 2
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
            totalAmount = BigDecimal("100.00"),
            paymentMethod = PaymentMethod.QR_CODE,
            products = listOf(orderProduct)
        )

        // When
        val entity = OrderEntityMapper.toEntity(order)

        // Then
        assertEquals(orderId, entity.id)
        assertEquals(createdBy, entity.createdBy)
        assertEquals(1, entity.orderStatus)
        assertEquals(0, entity.paymentMethod)
        assertEquals(BigDecimal("100.00"), entity.totalAmount)
        assertEquals(1, entity.products.size)
        assertFalse(entity.deleted)
    }

    @Test
    fun `should map OrderJpaEntity to Order domain`() {
        // Given
        val orderId = UUID.randomUUID()
        val createdBy = UUID.randomUUID()

        val orderJpa = OrderJpaEntity(
            id = orderId,
            orderConfirmationTime = null,
            paymentMethod = 0,
            orderStatus = 1,
            totalAmount = BigDecimal("100.00"),
            products = mutableListOf(),
            createdAt = LocalDateTime.now(),
            updatedAt = null,
            createdBy = createdBy,
            updatedBy = null,
            deleted = false
        )

        // When
        val domain = OrderEntityMapper.toDomain(orderJpa)

        // Then
        assertEquals(orderId, domain.id)
        assertEquals(createdBy, domain.createdBy)
        assertEquals(OrderStatus.CREATED, domain.orderStatus)
        assertEquals(PaymentMethod.QR_CODE, domain.paymentMethod)
        assertEquals(BigDecimal("100.00"), domain.totalAmount)
        assertFalse(domain.deleted)
    }

    @Test
    fun `should map Page of OrderJpaEntity to Paged Order domain`() {
        // Given
        val order1Jpa = OrderJpaEntity(
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

        val order2Jpa = OrderJpaEntity(
            id = UUID.randomUUID(),
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

        val pageable = PageRequest.of(0, 10)
        val pageJpa = PageImpl(listOf(order1Jpa, order2Jpa), pageable, 2)

        // When
        val result = OrderEntityMapper.toPagedDomain(pageJpa)

        // Then
        assertEquals(2, result.items.size)
        assertEquals(0, result.page)
        assertEquals(10, result.pageSize)
        assertEquals(2, result.totalItems)
        assertEquals(1, result.totalPages)
        assertEquals(OrderStatus.CREATED, result.items[0].orderStatus)
        assertEquals(OrderStatus.RECEIVED, result.items[1].orderStatus)
    }
}

