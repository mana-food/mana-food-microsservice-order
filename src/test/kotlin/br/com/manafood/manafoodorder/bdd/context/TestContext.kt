package br.com.manafood.manafoodorder.bdd.context

import br.com.manafood.manafoodorder.adapter.response.OrderResponse
import br.com.manafood.manafoodorder.domain.common.Paged
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import java.util.*

@Component
class TestContext {
    var createdOrderId: UUID? = null
    var lastOrderResponse: OrderResponse? = null
    var lastException: Exception? = null
    var productIds: MutableList<UUID> = mutableListOf()
    var lastPagedResponse: Paged<OrderResponse>? = null
    var lastResponseEntity: ResponseEntity<*>? = null

    fun reset() {
        createdOrderId = null
        lastOrderResponse = null
        lastException = null
        productIds.clear()
        lastPagedResponse = null
        lastResponseEntity = null
    }
}

