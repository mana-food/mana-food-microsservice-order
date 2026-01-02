package br.com.manafood.manafoodorder.application.factory

import br.com.manafood.manafoodorder.application.dto.OrderProductRequest
import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

/**
 * Factory responsible for creating OrderProduct domain objects.
 *
 * Following Single Responsibility Principle (SRP):
 * - Centralizes the logic for creating OrderProduct entities
 * - Separates object creation from business logic
 *
 * Following Open/Closed Principle (OCP):
 * - Can be extended with different creation strategies without modifying existing code
 */
@Component
class OrderProductFactory {

    /**
     * Creates an OrderProduct from a product request and product details.
     *
     * @param request The product request containing productId and quantity
     * @param product The product details from Product Service
     * @param createdBy The user creating the order
     * @param orderId The order this product belongs to
     * @return OrderProduct domain entity
     */
    fun createOrderProduct(
        request: OrderProductRequest,
        product: ProductDTO,
        createdBy: UUID,
        orderId: UUID
    ): OrderProduct {
        return OrderProduct(
            id = UUID.randomUUID(),
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = orderId,
            productId = product.id,
            productName = product.name,
            unitPrice = product.price,
            quantity = request.quantity,
            subtotal = product.price * request.quantity
        )
    }
}

