package br.com.manafood.manafoodorder.infrastructure.config

import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.infrastructure.persistence.adapter.OrderJpaRepositoryAdapter
import br.com.manafood.manafoodorder.infrastructure.persistence.repository.OrderJpaRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RepositoryConfig {

    @Bean
    fun orderRepository(
        springRepo: OrderJpaRepository
    ): OrderRepository = OrderJpaRepositoryAdapter(springRepo)

}
