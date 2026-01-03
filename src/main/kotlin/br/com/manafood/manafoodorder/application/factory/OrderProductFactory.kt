package br.com.manafood.manafoodorder.application.factory

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderProductCommand
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
class OrderProductFactory {

    fun createOrderProduct(
        request: CreateOrderProductCommand,
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
            unitPrice = product.unitPrice,
            quantity = request.quantity,
            subtotal = product.unitPrice * request.quantity
        )
    }
}
