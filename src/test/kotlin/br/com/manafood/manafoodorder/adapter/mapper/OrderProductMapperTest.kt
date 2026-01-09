package br.com.manafood.manafoodorder.adapter.mapper

import br.com.manafood.manafoodorder.adapter.request.commands.create.OrderProductRequest
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderProductMapperTest {

    @Test
    fun `should map OrderProductRequest list to CreateOrderProductCommand list`() {
        // Given
        val productId1 = UUID.randomUUID()
        val productId2 = UUID.randomUUID()
        val requestList = listOf(
            OrderProductRequest(productId = productId1, quantity = 2),
            OrderProductRequest(productId = productId2, quantity = 3)
        )

        // When
        val result = OrderProductMapper.toCreateCommandList(requestList)

        // Then
        assertEquals(2, result.size)
        assertEquals(productId1, result[0].productId)
        assertEquals(2, result[0].quantity)
        assertEquals(productId2, result[1].productId)
        assertEquals(3, result[1].quantity)
    }

    @Test
    fun `should map empty OrderProductRequest list`() {
        // Given
        val requestList = emptyList<OrderProductRequest>()

        // When
        val result = OrderProductMapper.toCreateCommandList(requestList)

        // Then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `should map OrderProduct list to OrderProductResponse list`() {
        // Given
        val orderId = UUID.randomUUID()
        val productId1 = UUID.randomUUID()
        val productId2 = UUID.randomUUID()

        val orderProduct1 = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = orderId,
            productId = productId1,
            productName = "Product 1",
            unitPrice = BigDecimal("25.00"),
            quantity = 2
        )

        val orderProduct2 = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = orderId,
            productId = productId2,
            productName = "Product 2",
            unitPrice = BigDecimal("30.00"),
            quantity = 1
        )

        val orderProducts = listOf(orderProduct1, orderProduct2)

        // When
        val result = OrderProductMapper.toResponseList(orderProducts)

        // Then
        assertEquals(2, result.size)
        assertEquals(productId1, result[0].productId)
        assertEquals(2, result[0].quantity)
        assertEquals(productId2, result[1].productId)
        assertEquals(1, result[1].quantity)
    }

    @Test
    fun `should map empty OrderProduct list`() {
        // Given
        val orderProducts = emptyList<OrderProduct>()

        // When
        val result = OrderProductMapper.toResponseList(orderProducts)

        // Then
        assertTrue(result.isEmpty())
    }
}

