package br.com.manafood.manafoodorder.infrastructure.client

import br.com.manafood.manafoodorder.infrastructure.client.dto.CategoryResponse
import br.com.manafood.manafoodorder.infrastructure.client.dto.ProductApiResponse
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.util.UUID

class ProductGatewayAdapterTest {

    private lateinit var productFeignClient: ProductFeignClient
    private lateinit var productGatewayAdapter: ProductGatewayAdapter

    @BeforeEach
    fun setUp() {
        productFeignClient = mockk()
        productGatewayAdapter = ProductGatewayAdapter(productFeignClient)
    }

    @Test
    fun `should get product by id successfully`() {
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
            description = "Description",
            unitPrice = BigDecimal("50.00"),
            category = categoryResponse
        )

        every { productFeignClient.getProductById(productId) } returns productApiResponse

        // When
        val result = productGatewayAdapter.getProductById(productId)

        // Then
        assertNotNull(result)
        assertEquals(productId, result.id)
        assertEquals("Test Product", result.name)
        assertEquals(BigDecimal("50.00"), result.unitPrice)
        assertEquals(categoryId, result.categoryId)
        verify(exactly = 1) { productFeignClient.getProductById(productId) }
    }
}

