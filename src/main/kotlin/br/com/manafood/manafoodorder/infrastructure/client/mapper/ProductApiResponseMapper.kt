package br.com.manafood.manafoodorder.infrastructure.client.mapper

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.infrastructure.client.dto.ProductApiResponse

object ProductApiResponseMapper {

    fun toApplicationDTO(response: ProductApiResponse) = ProductDTO(
        id = response.id,
        name = response.name,
        description = response.description,
        unitPrice = response.unitPrice,
        categoryId = response.category.id
    )
}