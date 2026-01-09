package br.com.manafood.manafoodorder.domain.exception

import java.util.UUID

class ProductNotFoundException(productId: UUID) : RuntimeException("Produto com id [$productId] não encontrado.")

