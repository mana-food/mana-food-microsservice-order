package br.com.manafood.manafoodorder.adapter.request.commands.update

import jakarta.validation.constraints.NotNull
import java.util.UUID

data class UpdateOrderRequest(
    @field:NotNull val id: UUID,
    @field:NotNull val orderStatus: Int
)
