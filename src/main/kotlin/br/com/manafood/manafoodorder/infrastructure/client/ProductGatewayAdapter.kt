package br.com.manafood.manafoodorder.infrastructure.client

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.application.gateway.ProductGateway
import br.com.manafood.manafoodorder.domain.exception.ProductNotFoundException
import br.com.manafood.manafoodorder.infrastructure.client.mapper.ProductApiResponseMapper
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class ProductGatewayAdapter(
    private val productFeignClient: ProductFeignClient
) : ProductGateway {

    override fun getProductById(productId: UUID): ProductDTO {
        return try {
            logger.info("$PREFIX Obtendo produto com id: [$productId] de Serviço de Produto")
            val productResponse = productFeignClient.getProductById(productId)
            logger.info("$PREFIX Produto encontrado: [${productResponse.name}]")
            return ProductApiResponseMapper.toApplicationDTO(productResponse)
        } catch (e: FeignException.NotFound) {
            logger.error("$PREFIX Produto com o id $productId não encontrado de Serviço de Produto", e)
            throw ProductNotFoundException(productId)
        } catch (e: FeignException) {
            logger.error("$PREFIX Erro na comunicação com o Serviço de Produto: ${e.message}", e)
            throw RuntimeException("$PREFIX Obtenção de erro do produto a partir do Serviço de Produto", e)
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java)
        private const val PREFIX = "[PRODUCT_GATEWAY_ADAPTER]"
    }
}
