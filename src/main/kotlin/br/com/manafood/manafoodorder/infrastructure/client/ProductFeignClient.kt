package br.com.manafood.manafoodorder.infrastructure.client

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.infrastructure.client.dto.ProductApiResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID

/**
 * Feign Client for Product Service communication.
 * This is an adapter in Clean Architecture - implements the ProductGateway port.
 *
 * The url is configured in application.yml under product.service.url
 */
@FeignClient(
    name = "product-service",
    url = "\${product.service.url}"
)
interface ProductFeignClient {

    @GetMapping("/api/products/{id}")
    fun getProductById(@PathVariable id: UUID): ProductApiResponse

    @GetMapping("/api/products/{id}/exists")
    fun existsById(@PathVariable id: UUID): Boolean
}

