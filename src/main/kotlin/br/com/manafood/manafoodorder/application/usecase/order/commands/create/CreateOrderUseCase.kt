package br.com.manafood.manafoodorder.application.usecase.order.commands.create

import br.com.manafood.manafoodorder.adapter.request.commands.create.OrderProductRequest
import br.com.manafood.manafoodorder.application.factory.OrderProductFactory
import br.com.manafood.manafoodorder.application.service.ProductValidationService
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

/**
 * Use Case for creating a new order.
 *
 * Following Single Responsibility Principle (SRP):
 * - This use case is responsible ONLY for orchestrating the order creation process
 * - Product validation is delegated to ProductValidationService
 * - OrderProduct creation is delegated to OrderProductFactory
 *
 * Following Dependency Inversion Principle (DIP):
 * - Depends on abstractions (interfaces) not concrete implementations
 * - Uses ProductValidationService and OrderProductFactory through constructor injection
 */
@Service
class CreateOrderUseCase(
    private val orderRepository: OrderRepository,
    private val productValidationService: ProductValidationService,
    private val orderProductFactory: OrderProductFactory
) {

    private val logger = LoggerFactory.getLogger(CreateOrderUseCase::class.java)

    /**
     * Executes the order creation use case.
     *
     * @param command The command containing order creation data
     * @return The created Order entity
     */
    fun execute(command: CreateOrderCommand): Order {
        logger.info("Creating order for user: ${command.createdBy}")

        // Generate order ID first (needed for creating order products)
        val orderId = UUID.randomUUID()

        // 1. Validate products and create order products
        val orderProducts = createOrderProducts(command.products, command.createdBy, orderId)

        // 2. Create the order entity
        val order = createOrderEntity(orderId, command, orderProducts)

        // 3. Calculate total amount
        val orderWithTotal = order.calculateTotal()

        // 4. Persist the order
        val savedOrder = orderRepository.save(orderWithTotal)

        logger.info("Order created successfully with id: ${savedOrder.id}, total: ${savedOrder.totalAmount}")
        return savedOrder
    }

    /**
     * Creates order products by validating each product and using the factory.
     *
     * @param productRequests List of product requests
     * @param createdBy User creating the order
     * @param orderId The order ID
     * @return List of validated OrderProduct entities
     */
    private fun createOrderProducts(
        productRequests: List<OrderProductRequest>,
        createdBy: UUID,
        orderId: UUID
    ): List<OrderProduct> {
        return productRequests.map { request ->
            // Validate product (throws exception if invalid)
            val product = productValidationService.validateAndGetProduct(request.productId)

            // Create OrderProduct using factory
            orderProductFactory.createOrderProduct(request, product, createdBy, orderId)
        }
    }

    /**
     * Creates the Order entity with initial values.
     *
     * @param orderId The order ID
     * @param command The creation command
     * @param orderProducts List of order products
     * @return Order entity
     */
    private fun createOrderEntity(
        orderId: UUID,
        command: CreateOrderCommand,
        orderProducts: List<OrderProduct>
    ): Order {
        return Order(
            id = orderId,
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
    }
}