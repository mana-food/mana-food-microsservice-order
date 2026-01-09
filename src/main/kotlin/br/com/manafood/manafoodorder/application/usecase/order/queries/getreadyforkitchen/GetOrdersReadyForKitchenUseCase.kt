package br.com.manafood.manafoodorder.application.usecase.order.queries.getreadyforkitchen

import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import org.slf4j.LoggerFactory

class GetOrdersReadyForKitchenUseCase(
    private val orderRepository: OrderRepository
) {

    fun execute(query: GetOrdersReadyForKitchenQuery): Paged<Order> {
        logger.debug("$PREFIX Iniciando a busca de todos os pedidos prontos na base de dados.")
        val ordersListFinded = orderRepository.findByOrdersReadyForKitchen(
            page = query.page,
            pageSize = query.pageSize
        )

        if(ordersListFinded.items.isEmpty()) {
            logger.debug("$PREFIX Foi/Foram encontrado(s) [${ordersListFinded.totalItems}] pedido(s) pronto(s) na base de dados.")
        } else {
            logger.debug("$PREFIX Não foi encontrado nenhum pedido pronto na base de dados.")
        }

        return ordersListFinded
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[GET_ORDERS_READY_FOR_KITCHEN_USE_CASE]"
    }
}