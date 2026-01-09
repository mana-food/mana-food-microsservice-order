package br.com.manafood.manafoodorder.application.service

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.application.gateway.ProductGateway
import br.com.manafood.manafoodorder.domain.exception.ProductNotFoundException
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.util.UUID

class ProductValidationServiceTest {

    private lateinit var productGateway: ProductGateway
    private lateinit var productValidationService: ProductValidationService

    @BeforeEach
    fun setUp() {
        productGateway = mockk()
        productValidationService = ProductValidationService(productGateway)
    }

    @Test
    fun `should validate and return product successfully`() {
        // Given
        val productId = UUID.randomUUID()
        val productDTO = ProductDTO(
            id = productId,
            name = "Test Product",
            description = "Product Description",
            unitPrice = BigDecimal("50.00"),
            categoryId = UUID.randomUUID()
        )

        every { productGateway.getProductById(productId) } returns productDTO

        // When
        val result = productValidationService.validateAndGetProduct(productId)

        // Then
        assertNotNull(result)
        assertEquals(productId, result.id)
        assertEquals("Test Product", result.name)
        assertEquals(BigDecimal("50.00"), result.unitPrice)
        verify(exactly = 1) { productGateway.getProductById(productId) }
    }

    @Test
    fun `should throw ProductNotFoundException when product not found`() {
        // Given
        val productId = UUID.randomUUID()

        every { productGateway.getProductById(productId) } throws ProductNotFoundException(productId)

        // When & Then
        assertThrows<ProductNotFoundException> {
            productValidationService.validateAndGetProduct(productId)
        }
        verify(exactly = 1) { productGateway.getProductById(productId) }
    }

    @Test
    fun `should propagate exception when gateway fails`() {
        // Given
        val productId = UUID.randomUUID()

        every { productGateway.getProductById(productId) } throws RuntimeException("Gateway error")

        // When & Then
        val exception = assertThrows<RuntimeException> {
            productValidationService.validateAndGetProduct(productId)
        }
        assertEquals("Gateway error", exception.message)
    }
}

