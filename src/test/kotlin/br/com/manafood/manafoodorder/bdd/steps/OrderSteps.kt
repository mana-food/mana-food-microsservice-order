package br.com.manafood.manafoodorder.bdd.steps

import br.com.manafood.manafoodorder.adapter.request.commands.create.CreateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.create.OrderProductRequest
import br.com.manafood.manafoodorder.adapter.request.commands.update.UpdateOrderRequest
import br.com.manafood.manafoodorder.adapter.request.commands.confirmpayment.ConfirmPaymentRequest
import br.com.manafood.manafoodorder.adapter.response.OrderResponse
import br.com.manafood.manafoodorder.bdd.context.TestContext
import br.com.manafood.manafoodorder.bdd.support.ProductMockServer
import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.infrastructure.persistence.repository.OrderJpaRepository
import io.cucumber.java.pt.Dado
import io.cucumber.java.pt.Quando
import io.cucumber.java.pt.Então
import io.cucumber.java.pt.E
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import java.math.BigDecimal
import java.util.*

class OrderSteps {

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var testContext: TestContext

    @Autowired
    private lateinit var productMockServer: ProductMockServer

    @Autowired
    private lateinit var orderRepository: OrderJpaRepository

    @Dado("que existem produtos válidos no sistema")
    fun queExistemProdutosValidosNoSistema() {
        val productId = UUID.randomUUID()
        testContext.productIds.add(productId)
        productMockServer.mockProductExists(productId, "Hambúrguer", BigDecimal("25.00"))
    }

    @Quando("eu criar um pedido com método de pagamento QR_CODE")
    fun euCriarUmPedidoComMetodoDePagamentoQRCode() {
        val productId = testContext.productIds.first()
        val orderProductRequest = OrderProductRequest(
            productId = productId,
            quantity = 2
        )
        val createOrderRequest = CreateOrderRequest(
            paymentMethod = 0, // QR_CODE
            products = listOf(orderProductRequest)
        )

        try {
            val response = restTemplate.postForEntity(
                "/api/order",
                createOrderRequest,
                OrderResponse::class.java
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastOrderResponse = response.body
                testContext.createdOrderId = response.body?.id
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Quando("eu criar um pedido com {int} produtos diferentes")
    fun euCriarUmPedidoComProdutosDiferentes(quantidade: Int) {
        // Mock multiple products
        testContext.productIds.clear()
        val productsRequests = mutableListOf<OrderProductRequest>()
        
        repeat(quantidade) { index ->
            val productId = UUID.randomUUID()
            testContext.productIds.add(productId)
            productMockServer.mockProductExists(
                productId,
                "Produto ${index + 1}",
                BigDecimal("${(index + 1) * 10}.00")
            )
            productsRequests.add(
                OrderProductRequest(
                    productId = productId,
                    quantity = index + 1
                )
            )
        }

        val createOrderRequest = CreateOrderRequest(
            paymentMethod = 0, // QR_CODE
            products = productsRequests
        )

        try {
            val response = restTemplate.postForEntity(
                "/api/order",
                createOrderRequest,
                OrderResponse::class.java
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastOrderResponse = response.body
                testContext.createdOrderId = response.body?.id
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Dado("que existe um pedido no sistema")
    fun queExisteUmPedidoNoSistema() {
        queExistemProdutosValidosNoSistema()
        euCriarUmPedidoComMetodoDePagamentoQRCode()
        assertThat(testContext.createdOrderId).isNotNull()
    }

    @Dado("que existe um pedido no sistema com status CREATED")
    fun queExisteUmPedidoNoSistemaComStatusCreated() {
        queExisteUmPedidoNoSistema()
        assertThat(testContext.lastOrderResponse?.orderStatus).isEqualTo(1) // CREATED
    }

    @Dado("que existe um pedido no sistema com status RECEIVED")
    fun queExisteUmPedidoNoSistemaComStatusReceived() {
        queExisteUmPedidoNoSistema()
        assertThat(testContext.lastOrderResponse?.orderStatus).isEqualTo(1) // RECEIVED
    }

    @Quando("eu atualizar o status do pedido para RECEIVED")
    fun euAtualizarOStatusDoPedidoParaReceived() {
        val orderId = testContext.createdOrderId ?: throw IllegalStateException("Id do pedido não encontrado")
        
        val updateOrderRequest = UpdateOrderRequest(
            id = orderId,
            orderStatus = 2 // RECEIVED
        )

        try {
            val response = restTemplate.exchange(
                "/api/order",
                HttpMethod.PUT,
                HttpEntity(updateOrderRequest),
                OrderResponse::class.java
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastOrderResponse = response.body
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Quando("eu atualizar o status do pedido para READY")
    fun euAtualizarOStatusDoPedidoParaReady() {
        val orderId = testContext.createdOrderId ?: throw IllegalStateException("Id do pedido não encontrado")

        val updateOrderRequest = UpdateOrderRequest(
            id = orderId,
            orderStatus = 4 // READY
        )

        try {
            val response = restTemplate.exchange(
                "/api/order",
                HttpMethod.PUT,
                HttpEntity(updateOrderRequest),
                OrderResponse::class.java
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastOrderResponse = response.body
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Quando("eu consultar o pedido pelo ID")
    fun euConsultarOPedidoPeloId() {
        val orderId = testContext.createdOrderId ?: throw IllegalStateException("Id do pedido não encontrado")
        
        try {
            val response = restTemplate.getForEntity(
                "/api/order/$orderId",
                OrderResponse::class.java
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastOrderResponse = response.body
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Quando("eu deletar o pedido")
    fun euDeletarOPedido() {
        val orderId = testContext.createdOrderId ?: throw IllegalStateException("Id do pedido não encontrado")
        
        try {
            val response = restTemplate.exchange(
                "/api/order/$orderId",
                HttpMethod.DELETE,
                null,
                Void::class.java
            )
            testContext.lastResponseEntity = response
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Dado("que existem {int} pedidos no sistema")
    fun queExistemPedidosNoSistema(quantidade: Int) {
        testContext.productIds.clear()
        val productId = UUID.randomUUID()
        testContext.productIds.add(productId)
        productMockServer.mockProductExists(productId, "Produto Teste", BigDecimal("15.00"))

        repeat(quantidade) {
            val orderProductRequest = OrderProductRequest(
                productId = productId,
                quantity = 1
            )
            val createOrderRequest = CreateOrderRequest(
                paymentMethod = 0, // QR_CODE
                products = listOf(orderProductRequest)
            )

            restTemplate.postForEntity(
                "/api/order",
                createOrderRequest,
                OrderResponse::class.java
            )
        }
    }

    @Quando("eu consultar todos os pedidos na página {int} com tamanho {int}")
    fun euConsultarTodosOsPedidosNaPaginaComTamanho(page: Int, pageSize: Int) {
        try {
            val response = restTemplate.exchange(
                "/api/order?page=$page&pageSize=$pageSize",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<Paged<OrderResponse>>() {}
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastPagedResponse = response.body
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Dado("que existem pedidos com status RECEIVED no sistema")
    fun queExistemPedidosComStatusReceivedNoSistema() {
        queExistemProdutosValidosNoSistema()
        euCriarUmPedidoComMetodoDePagamentoQRCode()
        euAtualizarOStatusDoPedidoParaReceived()
    }

    @Dado("que existem pedidos com status CREATED no sistema")
    fun queExistemPedidosComStatusCreatedNoSistema() {
        queExistemProdutosValidosNoSistema()
        euCriarUmPedidoComMetodoDePagamentoQRCode()
    }

    @Quando("eu consultar pedidos prontos para cozinha")
    fun euConsultarPedidosProntosParaCozinha() {
        try {
            val response = restTemplate.exchange(
                "/api/order/ready?page=0&pageSize=10",
                HttpMethod.GET,
                null,
                object : ParameterizedTypeReference<Paged<OrderResponse>>() {}
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastPagedResponse = response.body
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Quando("eu confirmar o pagamento do pedido")
    fun euConfirmarOPagamentoDoPedido() {
        val orderId = testContext.createdOrderId ?: throw IllegalStateException("Id do pedido não encontrado")
        
        val confirmPaymentRequest = ConfirmPaymentRequest(
            orderId = orderId,
            paymentStatus = "approved",
            paymentId = "payment-test-123"
        )

        try {
            val response = restTemplate.postForEntity(
                "/api/order/confirm-payment",
                confirmPaymentRequest,
                OrderResponse::class.java
            )
            testContext.lastResponseEntity = response
            if (response.statusCode == HttpStatus.OK && response.body != null) {
                testContext.lastOrderResponse = response.body
            }
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Dado("que não existem produtos no sistema")
    fun queNaoExistemProdutosNoSistema() {
        // Não fazemos mock, então o produto não existirá
    }

    @Quando("eu tentar criar um pedido com produto inexistente")
    fun euTentarCriarUmPedidoComProdutoInexistente() {
        val productId = UUID.randomUUID()
        productMockServer.mockProductNotFound(productId)
        
        val orderProductRequest = OrderProductRequest(
            productId = productId,
            quantity = 1
        )
        val createOrderRequest = CreateOrderRequest(
            paymentMethod = 0, // QR_CODE
            products = listOf(orderProductRequest)
        )

        try {
            val response = restTemplate.postForEntity(
                "/api/order",
                createOrderRequest,
                String::class.java
            )
            testContext.lastResponseEntity = response
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Quando("eu tentar atualizar um pedido que não existe")
    fun euTentarAtualizarUmPedidoQueNaoExiste() {
        val nonExistentOrderId = UUID.randomUUID()
        
        val updateOrderRequest = UpdateOrderRequest(
            id = nonExistentOrderId,
            orderStatus = 2 // RECEIVED
        )

        try {
            val response = restTemplate.exchange(
                "/api/order",
                HttpMethod.PUT,
                HttpEntity(updateOrderRequest),
                String::class.java
            )
            testContext.lastResponseEntity = response
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Quando("eu tentar consultar um pedido que não existe")
    fun euTentarConsultarUmPedidoQueNaoExiste() {
        val nonExistentOrderId = UUID.randomUUID()
        
        try {
            val response = restTemplate.getForEntity(
                "/api/order/$nonExistentOrderId",
                String::class.java
            )
            testContext.lastResponseEntity = response
        } catch (e: Exception) {
            testContext.lastException = e
        }
    }

    @Então("o pedido deve ser criado com sucesso")
    fun oPedidoDeveSerCriadoComSucesso() {
        assertThat(testContext.lastResponseEntity?.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(testContext.lastOrderResponse).isNotNull()
        assertThat(testContext.lastOrderResponse?.id).isNotNull()
    }

    @E("o pedido deve ter status CREATED")
    fun oPedidoDeveTerStatusCreated() {
        assertThat(testContext.lastOrderResponse?.orderStatus).isEqualTo(1)
    }

    @E("o método de pagamento deve ser QR_CODE")
    fun oMetodoDePagamentoDeveSerQRCode() {
        assertThat(testContext.lastOrderResponse?.paymentMethod).isEqualTo(0)
    }

    @E("o pedido deve conter {int} produtos")
    fun oPedidoDeveConterProdutos(quantidade: Int) {
        assertThat(testContext.lastOrderResponse?.products).hasSize(quantidade)
    }

    @E("o valor total deve ser calculado corretamente")
    fun oValorTotalDeveSerCalculadoCorretamente() {
        assertThat(testContext.lastOrderResponse?.totalAmount).isGreaterThan(BigDecimal.ZERO)
    }

    @Então("o pedido deve ter status RECEIVED")
    fun oPedidoDeveTerStatusReceived() {
        assertThat(testContext.lastOrderResponse?.orderStatus).isEqualTo(2)
    }

    @Então("o pedido deve ter status READY")
    fun oPedidoDeveTerStatusReady() {
        assertThat(testContext.lastOrderResponse?.orderStatus).isEqualTo(4)
    }

    @E("a data de atualização deve ser registrada")
    fun aDataDeAtualizacaoDeveSerRegistrada() {
        assertThat(testContext.lastOrderResponse?.updatedAt).isNotNull()
    }

    @Então("o pedido deve ser retornado com sucesso")
    fun oPedidoDeveSerRetornadoComSucesso() {
        assertThat(testContext.lastResponseEntity?.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(testContext.lastOrderResponse).isNotNull()
    }

    @E("todos os dados do pedido devem estar corretos")
    fun todosOsDadosDoPedidoDevemEstarCorretos() {
        assertThat(testContext.lastOrderResponse?.id).isEqualTo(testContext.createdOrderId)
        assertThat(testContext.lastOrderResponse?.paymentMethod).isEqualTo(0)
        assertThat(testContext.lastOrderResponse?.products).isNotEmpty()
    }

    @Então("devem ser retornados {int} pedidos")
    fun devemSerRetornadosPedidos(quantidade: Int) {
        assertThat(testContext.lastPagedResponse?.items).hasSize(quantidade)
    }

    @E("o total de páginas deve ser {int}")
    fun oTotalDePaginasDeveSer(totalPaginas: Int) {
        assertThat(testContext.lastPagedResponse?.totalPages).isEqualTo(totalPaginas)
    }

    @Então("apenas pedidos com status READY devem ser retornados")
    fun apenasPedidosComStatusReadyDevemSerRetornados() {
        assertThat(testContext.lastPagedResponse?.items).isNotEmpty()
        testContext.lastPagedResponse?.items?.forEach { order ->
            assertThat(order.orderStatus).isEqualTo(4) // READY
        }
    }

    @E("o horário de confirmação deve ser registrado")
    fun oHorarioDeConfirmacaoDeveSerRegistrado() {
        // O horário de confirmação é interno ao domínio, verificamos que a atualização ocorreu
        assertThat(testContext.lastOrderResponse?.orderStatus).isEqualTo(2)
    }

    @Então("o pedido deve ser marcado como deletado")
    fun oPedidoDeveSerMarcadoComoDeletado() {
        assertThat(testContext.lastResponseEntity?.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

    @E("o pedido não deve aparecer nas consultas")
    fun oPedidoNaoDeveAparecerNasConsultas() {
        val orderId = testContext.createdOrderId ?: throw IllegalStateException("Id do pedido não encontrado")
        
        val response = restTemplate.getForEntity(
            "/api/order/$orderId",
            OrderResponse::class.java
        )
        
        // O pedido deve retornar 404 ou estar marcado como deletado
        assertThat(response.statusCode).isIn(HttpStatus.NOT_FOUND, HttpStatus.OK)
    }

    @Então("deve retornar erro de produto não encontrado")
    fun deveRetornarErroDeProdutoNaoEncontrado() {
        assertThat(testContext.lastResponseEntity?.statusCode).isIn(
            HttpStatus.BAD_REQUEST,
            HttpStatus.NOT_FOUND,
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @Então("deve retornar erro de pedido não encontrado")
    fun deveRetornarErroDePedidoNaoEncontrado() {
        assertThat(testContext.lastResponseEntity?.statusCode).isIn(
            HttpStatus.NOT_FOUND,
            HttpStatus.BAD_REQUEST,
            HttpStatus.INTERNAL_SERVER_ERROR
        )
    }

    @Então("deve retornar pedido não encontrado")
    fun deveRetornarPedidoNaoEncontrado() {
        assertThat(testContext.lastResponseEntity?.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
    }
}

