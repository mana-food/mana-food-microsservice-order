package br.com.manafood.manafoodorder.infrastructure.config

import br.com.manafood.manafoodorder.application.factory.OrderProductFactory
import br.com.manafood.manafoodorder.application.service.ProductValidationService
import br.com.manafood.manafoodorder.application.usecase.order.commands.create.CreateOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.delete.DeleteOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.commands.update.UpdateOrderUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getall.GetAllOrdersUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getbyid.GetOrderByIdUseCase
import br.com.manafood.manafoodorder.application.usecase.order.queries.getreadyforkitchen.GetOrdersReadyForKitchenUseCase
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OrderUseCaseConfig(
    private val orderRepository: OrderRepository,
    private val productValidationService: ProductValidationService,
    private val orderProductFactory: OrderProductFactory
) {

    @Bean
    fun createOrderUseCase(): CreateOrderUseCase {
        return CreateOrderUseCase(orderRepository, productValidationService, orderProductFactory)
    }

    @Bean
    fun updateOrderUseCase(): UpdateOrderUseCase {
        return UpdateOrderUseCase(orderRepository)
    }

    @Bean
    fun deleteOrderUseCase(): DeleteOrderUseCase {
        return DeleteOrderUseCase(orderRepository)
    }

    @Bean
    fun getOrderByIdUseCase(): GetOrderByIdUseCase {
        return GetOrderByIdUseCase(orderRepository)
    }

    @Bean
    fun getAllOrdersUseCase(): GetAllOrdersUseCase {
        return GetAllOrdersUseCase(orderRepository)
    }

    @Bean
    fun getOrdersReadyForKitchenUseCase(): GetOrdersReadyForKitchenUseCase {
        return GetOrdersReadyForKitchenUseCase(orderRepository)
    }
}