package br.com.manafood.manafoodorder.application.usecase.order.commands.delete

import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException

class DeleteOrderUseCase(
    private val orderRepository: OrderRepository
) {
    
    fun execute(command: DeleteOrderCommand) {
        val orderFinded = orderRepository.findById(command.id)
        if(orderFinded == null) {
            logger.warn("$PREFIX Pedido não encontrado para o id [${command.id}].")
            throw IllegalArgumentException("$PREFIX Pedido não encontrado para o id [${command.id}].")
        }

        val deleted = orderFinded.copy(
            deleted = true,
            updatedBy = command.deletedBy,
            updatedAt = command.deletedAt
        )

        try {
            orderRepository.save(deleted)
            logger.debug("{} O pedido foi deletado com sucesso: [{}]", PREFIX, orderFinded.id)
        } catch (ex: DataAccessException) {
            logger.error("$PREFIX Falha ao tentar deletar o pedido: [${orderFinded.id}]")
            throw Exception("$PREFIX Falha ao tentar deletar o pedido", ex)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[DELETE_order_USE_CASE]"
    }
}
