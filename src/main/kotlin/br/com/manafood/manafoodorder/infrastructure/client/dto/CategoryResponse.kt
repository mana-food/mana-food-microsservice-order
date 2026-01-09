package br.com.manafood.manafoodorder.infrastructure.client.dto

import java.util.UUID

data class CategoryResponse(
    val id: UUID,
    val name: String
)
