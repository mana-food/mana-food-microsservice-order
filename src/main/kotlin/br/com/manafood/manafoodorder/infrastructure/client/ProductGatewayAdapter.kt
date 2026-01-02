package br.com.manafood.manafoodorder.infrastructure.client

import br.com.manafood.manafoodorder.application.gateway.ProductGateway
import br.com.manafood.manafoodorder.application.gateway.ProductResponse
import br.com.manafood.manafoodorder.domain.exception.ProductNotFoundException
import feign.FeignException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.UUID

/**
 * Adapter that implements ProductGateway using Feign Client.
 * This adapter connects the application layer (ProductGateway port)
 * to the infrastructure layer (ProductFeignClient).
 */
@Component
class ProductGatewayAdapter(
    private val productFeignClient: ProductFeignClient
) : ProductGateway {

    private val logger = LoggerFactory.getLogger(ProductGatewayAdapter::class.java)

    override fun getProductById(productId: UUID): ProductResponse {
        return try {
            logger.info("Fetching product with id: $productId from Product Service")
            val product = productFeignClient.getProductById(productId)
            logger.info("Product found: ${product.name}")
            product
        } catch (e: FeignException.NotFound) {
            logger.error("Product with id $productId not found in Product Service")
            throw ProductNotFoundException(productId)
        } catch (e: FeignException) {
            logger.error("Error communicating with Product Service: ${e.message}", e)
            throw RuntimeException("Error fetching product from Product Service", e)
        }
    }

    override fun existsById(productId: UUID): Boolean {
        return try {
            logger.info("Checking if product with id: $productId exists")
            val exists = productFeignClient.existsById(productId)
            logger.info("Product $productId exists: $exists")
            exists
        } catch (e: FeignException.NotFound) {
            logger.info("Product with id $productId does not exist")
            false
        } catch (e: FeignException) {
            logger.error("Error checking product existence: ${e.message}", e)
            throw RuntimeException("Error checking product existence", e)
        }
    }
}

