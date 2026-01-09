package br.com.manafood.manafoodorder.infrastructure.client.mapper

import br.com.manafood.manafoodorder.infrastructure.client.dto.CategoryResponse
import br.com.manafood.manafoodorder.infrastructure.client.dto.ProductApiResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class ProductApiResponseMapperTest {

    @Test
    fun `should map ProductApiResponse to ProductDTO`() {
        // Given
        val productId = UUID.randomUUID()
        val categoryId = UUID.randomUUID()

        val categoryResponse = CategoryResponse(
            id = categoryId,
            name = "Test Category"
        )

        val productApiResponse = ProductApiResponse(
            id = productId,
            name = "Test Product",
            description = "Product Description",
            unitPrice = BigDecimal("50.00"),
            category = categoryResponse
        )

        // When
        val result = ProductApiResponseMapper.toApplicationDTO(productApiResponse)

        // Then
        assertEquals(productId, result.id)
        assertEquals("Test Product", result.name)
        assertEquals("Product Description", result.description)
        assertEquals(BigDecimal("50.00"), result.unitPrice)
        assertEquals(categoryId, result.categoryId)
    }

    @Test
    fun `should map ProductApiResponse with empty description`() {
        // Given
        val productId = UUID.randomUUID()
        val categoryId = UUID.randomUUID()

        val categoryResponse = CategoryResponse(
            id = categoryId,
            name = "Test Category"
        )

        val productApiResponse = ProductApiResponse(
            id = productId,
            name = "Test Product",
            description = "",
            unitPrice = BigDecimal("75.50"),
            category = categoryResponse
        )

        // When
        val result = ProductApiResponseMapper.toApplicationDTO(productApiResponse)

        // Then
        assertEquals(productId, result.id)
        assertEquals("Test Product", result.name)
        assertEquals("", result.description)
        assertEquals(BigDecimal("75.50"), result.unitPrice)
        assertEquals(categoryId, result.categoryId)
    }
}

