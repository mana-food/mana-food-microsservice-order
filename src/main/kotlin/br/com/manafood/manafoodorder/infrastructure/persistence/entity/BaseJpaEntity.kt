package br.com.manafood.manafoodorder.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.time.LocalDateTime
import java.util.*

@MappedSuperclass
abstract class BaseJpaEntity(

    @Id
    @Column(nullable = false, updatable = false, length = 36)
    val id: UUID,

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(nullable = false, updatable = false)
    val createdBy: UUID,

    @Column
    val updatedAt: LocalDateTime? = null,

    @Column
    val updatedBy: UUID? = null,

    @Column(nullable = false)
    val deleted: Boolean = false
)
