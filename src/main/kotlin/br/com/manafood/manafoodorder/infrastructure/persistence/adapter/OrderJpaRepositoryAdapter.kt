package br.com.manafood.manafoodorder.infrastructure.persistence.adapter

import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.infrastructure.persistence.repository.OrderJpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OrderJpaRepositoryAdapter(
    private val springDataRepository: OrderJpaRepository
) : OrderRepository {
    override fun findById(id: UUID): Order? {
        TODO("Not yet implemented")
    }

    override fun findPaged(
        page: Int,
        pageSize: Int
    ): Paged<Order> {
        TODO("Not yet implemented")
    }

    override fun findByIds(ids: List<UUID>): List<Order> {
        TODO("Not yet implemented")
    }

    override fun save(entity: Order): Order {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: UUID): Boolean {
        TODO("Not yet implemented")
    }
}