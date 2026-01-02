package br.com.manafood.manafoodorder.domain.exception

import java.util.UUID

class ProductNotFoundException(productId: UUID) : RuntimeException("Product with id $productId not found")

