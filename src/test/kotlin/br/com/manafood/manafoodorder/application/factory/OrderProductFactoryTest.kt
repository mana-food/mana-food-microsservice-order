package br.com.manafood.manafoodorder.application.factory

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderProductCommand
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class OrderProductFactoryTest {

    private lateinit var orderProductFactory: OrderProductFactory

    @BeforeEach
    fun setUp() {
        orderProductFactory = OrderProductFactory()
    }

    @Test
    fun `should create order product with correct properties`() {
        // Given
        val productId = UUID.randomUUID()
        val productDTO = ProductDTO(
            id = productId,
            name = "Test Product",
            description = "Product Description",
            unitPrice = BigDecimal("25.00"),
            categoryId = UUID.randomUUID()
        )

        val request = CreateOrderProductCommand(
            productId = productId,
            quantity = 3
        )

        val createdBy = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        // When
        val result = orderProductFactory.createOrderProduct(request, productDTO, createdBy, orderId)

        // Then
        assertNotNull(result)
        assertNotNull(result.id)
        assertEquals(createdBy, result.createdBy)
        assertEquals(orderId, result.orderId)
        assertEquals(productId, result.productId)
        assertEquals("Test Product", result.productName)
        assertEquals(BigDecimal("25.00"), result.unitPrice)
        assertEquals(3, result.quantity)
        assertEquals(0, BigDecimal("75.00").compareTo(result.subtotal))
        assertFalse(result.deleted)
        assertNull(result.updatedBy)
        assertNull(result.updatedAt)
    }

    @Test
    fun `should create order product with single quantity`() {
        // Given
        val productId = UUID.randomUUID()
        val productDTO = ProductDTO(
            id = productId,
            name = "Single Product",
            description = "Description",
            unitPrice = BigDecimal("100.00"),
            categoryId = UUID.randomUUID()
        )

        val request = CreateOrderProductCommand(
            productId = productId,
            quantity = 1
        )

        val createdBy = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        // When
        val result = orderProductFactory.createOrderProduct(request, productDTO, createdBy, orderId)

        // Then
        assertEquals(1, result.quantity)
        assertEquals(0, BigDecimal("100.00").compareTo(result.subtotal))
    }

    @Test
    fun `should create order product with decimal price`() {
        // Given
        val productId = UUID.randomUUID()
        val productDTO = ProductDTO(
            id = productId,
            name = "Decimal Product",
            description = "Description",
            unitPrice = BigDecimal("15.75"),
            categoryId = UUID.randomUUID()
        )

        val request = CreateOrderProductCommand(
            productId = productId,
            quantity = 4
        )

        val createdBy = UUID.randomUUID()
        val orderId = UUID.randomUUID()

        // When
        val result = orderProductFactory.createOrderProduct(request, productDTO, createdBy, orderId)

        // Then
        assertEquals(BigDecimal("15.75"), result.unitPrice)
        assertEquals(4, result.quantity)
        assertEquals(0, BigDecimal("63.00").compareTo(result.subtotal))
    }
}

