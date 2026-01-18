package br.com.manafood.manafoodorder.infrastructure.client

import br.com.manafood.manafoodorder.infrastructure.client.dto.ProductApiResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import java.util.UUID


@FeignClient(
    name = "product-service",
    url = "\${PRODUCT_SERVICE_URL}"
)
interface ProductFeignClient {

    @GetMapping("/api/products/{id}")
    fun getProductById(@PathVariable id: UUID): ProductApiResponse

    @GetMapping("/api/products/{id}/exists")
    fun existsById(@PathVariable id: UUID): Boolean
}

