package br.com.manafood.manafoodorder.application.gateway

import java.math.BigDecimal
import java.util.UUID

/**
 * Gateway interface for Product service communication.
 * This is a port in Clean Architecture - defines what the application needs from external services.
 */
interface ProductGateway {

    /**
     * Get product details by product ID.
     * @param productId The product UUID
     * @return ProductResponse with product details
     * @throws ProductNotFoundException if product not found
     */
    fun getProductById(productId: UUID): ProductResponse

    /**
     * Check if a product exists.
     * @param productId The product UUID
     * @return true if product exists, false otherwise
     */
    fun existsById(productId: UUID): Boolean
}

/**
 * Response DTO from Product service
 */
data class ProductResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val price: BigDecimal,
    val categoryId: UUID,
    val active: Boolean
)

