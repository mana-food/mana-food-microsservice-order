package br.com.manafood.manafoodorder.infrastructure.persistence.adapter

import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.infrastructure.persistence.mapper.OrderEntityMapper
import br.com.manafood.manafoodorder.infrastructure.persistence.repository.OrderJpaRepository
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class OrderJpaRepositoryAdapter(
    private val springDataRepository: OrderJpaRepository
) : OrderRepository {

    override fun save(entity: Order): Order {
        val saved = springDataRepository.save(
            OrderEntityMapper.toEntity(entity)
        )
        return OrderEntityMapper.toDomain(saved)
    }

    override fun deleteById(id: UUID): Boolean {
        springDataRepository.deleteById(id)

        val exists = springDataRepository.existsByIdAndNotDeleted(id)
        return !exists
    }

    override fun findById(id: UUID): Order? {
        return springDataRepository.findByIdAndNotDeleted(id)
            .map { OrderEntityMapper.toDomain(it) }
            .orElse(null)
    }

    override fun findByIds(ids: List<UUID>): List<Order> {
        return springDataRepository.findAllByIdAndNotDeleted(ids)
            .map { OrderEntityMapper.toDomain(it) }
    }

    override fun findPaged(
        page: Int,
        pageSize: Int
    ): Paged<Order> {
        val pageable = PageRequest.of(page, pageSize)
        val ordersPaged = springDataRepository.findPaged(pageable)

        return OrderEntityMapper.toPagedDomain(ordersPaged)
    }

    override fun findByOrdersReadyForKitchen(
        page: Int,
        pageSize: Int
    ): Paged<Order> {
        val pageable = PageRequest.of(page, pageSize)
        val ordersPaged = springDataRepository.findByOrdersReadyForKitchenPaged(pageable)

        return OrderEntityMapper.toPagedDomain(ordersPaged)
    }
}
