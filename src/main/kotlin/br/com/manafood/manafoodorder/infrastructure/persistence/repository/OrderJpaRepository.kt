package br.com.manafood.manafoodorder.infrastructure.persistence.repository

import br.com.manafood.manafoodorder.infrastructure.persistence.entity.OrderJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.Optional
import java.util.UUID

interface OrderJpaRepository : JpaRepository<OrderJpaEntity, UUID> {

    @Query("SELECT o FROM OrderJpaEntity o WHERE o.deleted = false")
    fun findPaged(pageable: Pageable): Page<OrderJpaEntity>

    @Query("SELECT o FROM OrderJpaEntity o WHERE o.id = :id AND o.deleted = false")
    fun findByIdAndNotDeleted(id: UUID): Optional<OrderJpaEntity>

    @Query("SELECT o FROM OrderJpaEntity o WHERE o.id IN :ids AND o.deleted = false")
    fun findAllByIdAndNotDeleted(ids: List<UUID>): List<OrderJpaEntity>

    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderJpaEntity o WHERE o.id = :id AND o.deleted = false")
    fun existsByIdAndNotDeleted(id: UUID): Boolean
    
    @Query("SELECT o FROM OrderJpaEntity o WHERE o.orderStatus = 4 AND o.deleted = false")
    fun findByOrdersReadyForKitchenPaged(pageable: Pageable): Page<OrderJpaEntity>
}
