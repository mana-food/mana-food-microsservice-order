package br.com.manafood.manafoodorder.infrastructure.persistence.repository

import br.com.manafood.manafoodorder.infrastructure.persistence.entity.OrderJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, UUID>  {
}