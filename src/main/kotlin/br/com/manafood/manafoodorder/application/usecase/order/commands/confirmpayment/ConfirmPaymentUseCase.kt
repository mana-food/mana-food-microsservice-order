package br.com.manafood.manafoodorder.application.usecase.order.commands.confirmpayment

import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import org.slf4j.LoggerFactory

class ConfirmPaymentUseCase(
    private val orderRepository: OrderRepository
) {
    fun execute(command: ConfirmPaymentCommand) {
        try {
            logger.info("$PREFIX Iniciando confirmação de pagamento para o pedido: ${command.orderId}")

            val orderFinded = orderRepository.findById(command.orderId)
                ?: throw IllegalArgumentException("$PREFIX Pedido com id ${command.orderId} não encontrado.")

            if(command.paymentStatus != "approved") {
                logger.info("$PREFIX Status de pagamento não é aprovado para o pedido: ${command.orderId}, status: ${command.paymentStatus}")
                throw IllegalArgumentException("$PREFIX Status de pagamento não é aprovado para o pedido: ${command.orderId}, status: ${command.paymentStatus}.")
            }

            orderFinded.updatedBy = command.updatedBy
            orderRepository.save(orderFinded.setStatus(OrderStatus.RECEIVED))
            logger.info("$PREFIX Pagamento confirmado com sucesso para o pedido: ${command.orderId}")
        } catch (ex: Exception) {
            logger.error("$PREFIX Falha ao tentar confirmar o pagamento para o pedido: ${command.orderId}", ex)
            throw Exception("$PREFIX Falha ao tentar confirmar o pagamento", ex)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[CONFIRM_PAYMENT_ORDER_USE_CASE]"
    }
}
