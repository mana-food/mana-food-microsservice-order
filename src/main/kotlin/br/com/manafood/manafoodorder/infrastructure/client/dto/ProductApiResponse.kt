package br.com.manafood.manafoodorder.infrastructure.client.dto

import java.math.BigDecimal
import java.util.UUID

data class ProductApiResponse(
    val id: UUID,
    val name: String,
    val description: String,
    val unitPrice: BigDecimal,
    val category: CategoryResponse
)