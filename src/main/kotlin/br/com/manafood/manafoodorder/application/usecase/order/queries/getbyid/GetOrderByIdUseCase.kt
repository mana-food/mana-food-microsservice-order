package br.com.manafood.manafoodorder.application.usecase.order.queries.getbyid

import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import org.slf4j.LoggerFactory
import org.springframework.dao.DataAccessException

class GetOrderByIdUseCase(
    private val orderRepository: OrderRepository
) {
    fun execute(query: GetOrderByIdQuery): Order? {
        try {
            logger.debug("{} Iniciando a busca de do pedido [{}] na base de dados.", PREFIX, query.id)
            val orderFinded = orderRepository.findById(query.id)

            if (orderFinded != null) {
                logger.debug("{} Pedido encontrado", PREFIX)
            } else {
                logger.debug(
                    "{} Pedido não encontrado para o id: {}",
                    PREFIX, query.id
                )
            }

            return orderFinded

        } catch (ex: DataAccessException) {
            logger.error("$PREFIX Falha ao tentar encontrar o produto do id ${query.id}")
            throw Exception("$PREFIX Falha ao tentar encontrar o produto do id ${query.id}", ex)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[GET_BY_ID_ORDER_USE_CASE]"
    }
}