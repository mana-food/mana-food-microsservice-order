package br.com.manafood.manafoodorder.domain.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class OrderProductTest {

    @Test
    fun `should calculate subtotal correctly`() {
        // Given
        val unitPrice = BigDecimal("25.50")
        val quantity = 3

        // When
        val orderProduct = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = UUID.randomUUID(),
            productId = UUID.randomUUID(),
            productName = "Test Product",
            unitPrice = unitPrice,
            quantity = quantity
        )

        // Then
        assertEquals(0, BigDecimal("76.50").compareTo(orderProduct.subtotal))
    }

    @Test
    fun `should create order product with correct properties`() {
        // Given
        val id = UUID.randomUUID()
        val createdBy = UUID.randomUUID()
        val orderId = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val productName = "Product Name"
        val unitPrice = BigDecimal("10.00")
        val quantity = 5

        // When
        val orderProduct = OrderProduct(
            id = id,
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = orderId,
            productId = productId,
            productName = productName,
            unitPrice = unitPrice,
            quantity = quantity
        )

        // Then
        assertEquals(id, orderProduct.id)
        assertEquals(createdBy, orderProduct.createdBy)
        assertEquals(orderId, orderProduct.orderId)
        assertEquals(productId, orderProduct.productId)
        assertEquals(productName, orderProduct.productName)
        assertEquals(unitPrice, orderProduct.unitPrice)
        assertEquals(quantity, orderProduct.quantity)
        assertEquals(0, BigDecimal("50.00").compareTo(orderProduct.subtotal))
    }

    @Test
    fun `should calculate subtotal with single item`() {
        // Given & When
        val orderProduct = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = UUID.randomUUID(),
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = UUID.randomUUID(),
            productId = UUID.randomUUID(),
            productName = "Test Product",
            unitPrice = BigDecimal("99.99"),
            quantity = 1
        )

        // Then
        assertEquals(0, BigDecimal("99.99").compareTo(orderProduct.subtotal))
    }
}

