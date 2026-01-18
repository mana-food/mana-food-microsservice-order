package br.com.manafood.manafoodorder.bdd.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.*

@Component
class ProductMockServer {

    private val wireMockServer: WireMockServer = WireMockServer(
        WireMockConfiguration.options()
            .port(8080)
    )

    private val objectMapper = ObjectMapper()

    init {
        if (!wireMockServer.isRunning) {
            wireMockServer.start()
        }
    }

    fun stop() {
        if (wireMockServer.isRunning) {
            wireMockServer.stop()
        }
    }

    fun reset() {
        wireMockServer.resetAll()
    }

    fun mockProductExists(productId: UUID, name: String = "Test Product", price: BigDecimal = BigDecimal("10.00")) {
        val categoryId = UUID.randomUUID()
        val productResponse = mapOf(
            "id" to productId.toString(),
            "name" to name,
            "description" to "Test product description",
            "unitPrice" to price.toDouble(),
            "category" to mapOf(
                "id" to categoryId.toString(),
                "name" to "Test Category"
            )
        )

        wireMockServer.stubFor(
            get(urlPathEqualTo("/api/products/$productId"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(productResponse))
                )
        )

        wireMockServer.stubFor(
            get(urlPathEqualTo("/api/products/$productId/exists"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("true")
                )
        )
    }

    fun mockProductNotFound(productId: UUID) {
        wireMockServer.stubFor(
            get(urlPathEqualTo("/api/products/$productId"))
                .willReturn(
                    aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"error\": \"Product not found\"}")
                )
        )

        wireMockServer.stubFor(
            get(urlPathEqualTo("/api/products/$productId/exists"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("false")
                )
        )
    }
}

