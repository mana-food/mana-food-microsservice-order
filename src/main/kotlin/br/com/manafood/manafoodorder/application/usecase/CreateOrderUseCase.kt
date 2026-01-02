package br.com.manafood.manafoodorder.application.usecase

import br.com.manafood.manafoodorder.application.gateway.ProductGateway
import br.com.manafood.manafoodorder.domain.exception.ProductNotFoundException
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Use Case for creating a new order.
 * This demonstrates how to use the ProductGateway (PORT) in Clean Architecture.
 */
@Service
class CreateOrderUseCase(
    private val orderRepository: OrderRepository,
    private val productGateway: ProductGateway  // ← Injeta o PORT, não o ADAPTER
) {

    private val logger = LoggerFactory.getLogger(CreateOrderUseCase::class.java)

    fun execute(command: CreateOrderCommand): Order {
        logger.info("Creating order for user: ${command.createdBy}")

        // 1. Validar e buscar informações dos produtos
        val orderProducts = command.products.map { productRequest ->
            validateAndCreateOrderProduct(productRequest, command.createdBy)
        }

        // 2. Criar o pedido
        val order = Order(
            id = UUID.randomUUID(),
            createdBy = command.createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderConfirmationTime = null,
            orderStatus = OrderStatus.CREATED,
            totalAmount = BigDecimal.ZERO,
            paymentMethod = command.paymentMethod,
            products = orderProducts
        )

        // 3. Calcular total
        val orderWithTotal = order.calculateTotal()

        // 4. Salvar no repositório
        val savedOrder = orderRepository.save(orderWithTotal)

        logger.info("Order created successfully with id: ${savedOrder.id}")
        return savedOrder
    }

    private fun validateAndCreateOrderProduct(
        productRequest: ProductOrderRequest,
        createdBy: UUID
    ): OrderProduct {
        // Busca o produto no Product Service usando o Gateway
        val product = try {
            productGateway.getProductById(productRequest.productId)
        } catch (e: ProductNotFoundException) {
            logger.error("Product not found: ${productRequest.productId}")
            throw e
        }

        // Valida se o produto está ativo
        if (!product.active) {
            throw IllegalArgumentException("Product ${product.id} is not active")
        }

        // Cria o OrderProduct com as informações do produto
        return OrderProduct(
            id = UUID.randomUUID(),
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = UUID.randomUUID(), // Será atualizado quando salvar a ordem
            productId = product.id,
            productName = product.name,
            unitPrice = product.price,
            quantity = productRequest.quantity,
            subtotal = product.price * productRequest.quantity
        )
    }
}

/**
 * Command DTO for creating an order
 */
data class CreateOrderCommand(
    val createdBy: UUID,
    val paymentMethod: PaymentMethod,
    val products: List<ProductOrderRequest>
)

/**
 * Product request in the order
 */
data class ProductOrderRequest(
    val productId: UUID,
    val quantity: BigDecimal
)

