package br.com.manafood.manafoodorder.application.dto

import java.math.BigDecimal
import java.util.UUID

data class ProductDTO(
    val id: UUID,
    val name: String,
    val description: String?,
    val unitPrice: BigDecimal,
    val categoryId: UUID
)
