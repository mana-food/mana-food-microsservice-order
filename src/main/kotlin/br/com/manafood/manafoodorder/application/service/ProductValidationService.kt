package br.com.manafood.manafoodorder.application.service

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.application.gateway.ProductGateway
import br.com.manafood.manafoodorder.domain.exception.ProductNotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class ProductValidationService(
    private val productGateway: ProductGateway
) {
    fun validateAndGetProduct(productId: UUID): ProductDTO {
        logger.debug("$PREFIX Validação do produto ID :[{}]", productId)

        try {
            val product = productGateway.getProductById(productId)
            logger.debug("$PREFIX Produto validado com sucesso: [${product.name}]")
            return product
        } catch (e: ProductNotFoundException) {
            logger.error("$PREFIX Produto não encontrado: [$productId]")
            throw e
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[PRODUCT_VALIDATION_SERVICE]"
    }
}

