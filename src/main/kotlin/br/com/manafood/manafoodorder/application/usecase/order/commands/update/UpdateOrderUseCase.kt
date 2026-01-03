package br.com.manafood.manafoodorder.application.usecase.order.commands.update

import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus.Companion.fromCode
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException

class UpdateOrderUseCase(
    private val orderRepository: OrderRepository
) {

    fun execute(command: UpdateOrderCommand): Order {
        logger.debug("{} Iniciando atualização do pedido: {}", PREFIX, command.id)

        val orderFinded = orderRepository.findById(command.id)
        if (orderFinded == null) {
            logger.warn("$PREFIX Pedido com id ${command.id} não encontrado.")
            throw IllegalArgumentException("$PREFIX Pedido com id ${command.id} não encontrado.")
        }

        val order = orderFinded.copy(
            orderStatus = fromCode(command.orderStatus)
        )

        try {
            val orderUpdated = orderRepository.save(order)
            logger.debug(
                "{} O pedido foi atualizado com sucesso, foi atualizado de [{}] para [{}]",
                PREFIX,
                orderFinded.orderStatus,
                fromCode(command.orderStatus)
            )
            return orderUpdated
        } catch (ex: DataAccessException) {
            logger.error("$PREFIX Falha ao tentar atualizar o pedido: ${command.id}")
            throw Exception("$PREFIX Erro ao atualizar entidade", ex)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[UPDATE_ORDER_USE_CASE]"
    }
}
