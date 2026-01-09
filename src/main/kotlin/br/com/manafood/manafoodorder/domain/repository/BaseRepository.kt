package br.com.manafood.manafoodorder.domain.repository

import br.com.manafood.manafoodorder.domain.common.BaseEntity
import br.com.manafood.manafoodorder.domain.common.Paged
import java.util.*

interface BaseRepository<T : BaseEntity> {

    fun findById(id: UUID): T?
    fun findPaged(page: Int, pageSize: Int): Paged<T>
    fun findByIds(ids: List<UUID>): List<T>
    fun save(entity: T): T
    fun deleteById(id: UUID): Boolean
}
