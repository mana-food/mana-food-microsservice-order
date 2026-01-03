package br.com.manafood.manafoodorder.domain.repository

import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order

interface OrderRepository : BaseRepository<Order> {

    fun findByOrdersReadyForKitchen(page: Int, pageSize: Int) : Paged<Order>
}
