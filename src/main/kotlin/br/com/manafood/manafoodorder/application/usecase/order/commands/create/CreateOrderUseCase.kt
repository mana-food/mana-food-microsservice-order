package br.com.manafood.manafoodorder.application.usecase.order.commands.create

import br.com.manafood.manafoodorder.application.factory.OrderProductFactory
import br.com.manafood.manafoodorder.application.service.ProductValidationService
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod.Companion.fromCode
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Service
class CreateOrderUseCase(
    private val orderRepository: OrderRepository,
    private val productValidationService: ProductValidationService,
    private val orderProductFactory: OrderProductFactory
) {

    fun execute(command: CreateOrderCommand): Order {
        try {
            logger.info("$PREFIX Iniciando criação do pedido pelo usuário: ${command.createdBy}")

            val orderId = UUID.randomUUID()
            val orderProducts = createOrderProducts(command.products, command.createdBy, orderId)
            val order = createOrderEntity(orderId, command, orderProducts)
            val orderWithTotal = order.calculateTotal()
            val savedOrder = orderRepository.save(orderWithTotal)
            logger.info(
                "$PREFIX Pedido criado com sucesso - ID: ${savedOrder.id}, total: R$ ${
                    savedOrder.totalAmount.setScale(
                        2
                    )
                }"
            )
            return savedOrder
        } catch (ex: DataAccessException) {
            logger.error(
                "$PREFIX Falha ao tentar criar o pedido: [Método de pagamento: ${command.paymentMethod} e Produtos: ${command.products}]", ex
            )
            throw Exception("$PREFIX Falha ao tentar criar o pedido", ex)
        }
    }

    private fun createOrderProducts(
        productCommands: List<CreateOrderProductCommand>,
        createdBy: UUID,
        orderId: UUID
    ): List<OrderProduct> {
        return productCommands.map { request ->
            val product = productValidationService.validateAndGetProduct(request.productId)
            orderProductFactory.createOrderProduct(request, product, createdBy, orderId)
        }
    }

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
            paymentMethod = fromCode(command.paymentMethod),
            products = orderProducts
        )
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[CREATE_ORDER_USE_CASE]"
    }
}
