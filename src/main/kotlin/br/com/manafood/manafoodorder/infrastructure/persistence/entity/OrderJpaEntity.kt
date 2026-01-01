package br.com.manafood.manafoodorder.infrastructure.persistence.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "order")
class OrderJpaEntity(

    id: UUID,

//    TODO: adicionar os atributos da entidade Order

    createdAt: LocalDateTime,
    createdBy: UUID,
    updatedAt: LocalDateTime? = null,
    updatedBy: UUID? = null,
    deleted: Boolean = false
) : BaseJpaEntity(
    id = id,
    createdAt = createdAt,
    createdBy = createdBy,
    updatedAt = updatedAt,
    updatedBy = updatedBy,
    deleted = deleted
)
