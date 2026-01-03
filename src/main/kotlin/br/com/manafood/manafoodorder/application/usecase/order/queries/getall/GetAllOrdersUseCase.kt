package br.com.manafood.manafoodorder.application.usecase.order.queries.getall

import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import org.slf4j.LoggerFactory

class GetAllOrdersUseCase(
    private val orderRepository: OrderRepository
) {
    fun execute(query: GetAllOrdersQuery): Paged<Order> {
        logger.debug("$PREFIX Iniciando a busca de todos os pedidos na base de dados.")
        val ordersListFinded = orderRepository.findPaged(
            page = query.page,
            pageSize = query.pageSize
        )

        if(ordersListFinded.items.isEmpty()) {
            logger.debug("$PREFIX Foi/Foram encontrado(s) [${ordersListFinded.totalItems}] pedido(s) na base de dados.")
        } else {
            logger.debug("$PREFIX Não foi encontrado nenhum pedido na base de dados.")
        }

        return ordersListFinded
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[GET_ALL_ORDERS_USE_CASE]"
    }
}