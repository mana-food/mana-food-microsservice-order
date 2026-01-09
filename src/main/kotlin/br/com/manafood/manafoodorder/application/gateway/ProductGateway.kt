package br.com.manafood.manafoodorder.application.gateway

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import java.util.UUID

interface ProductGateway {
    fun getProductById(productId: UUID): ProductDTO
}


