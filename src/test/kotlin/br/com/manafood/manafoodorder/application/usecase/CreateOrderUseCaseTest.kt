package br.com.manafood.manafoodorder.application.usecase

import br.com.manafood.manafoodorder.application.gateway.ProductGateway
import br.com.manafood.manafoodorder.application.gateway.ProductResponse
import br.com.manafood.manafoodorder.domain.exception.ProductNotFoundException
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.util.UUID

/**
 * Unit test for CreateOrderUseCase.
 * Demonstrates how to mock the ProductGateway (PORT) for testing.
 */
class CreateOrderUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var productGateway: ProductGateway
    private lateinit var createOrderUseCase: CreateOrderUseCase

    @BeforeEach
    fun setup() {
        // Mock das dependências
        orderRepository = mockk(relaxed = true)
        productGateway = mockk()

        // Instancia o Use Case com os mocks
        createOrderUseCase = CreateOrderUseCase(orderRepository, productGateway)
    }

    @Test
    fun `should create order when product exists and is active`() {
        // Given
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val productResponse = ProductResponse(
            id = productId,
            name = "Test Product",
            description = "Test Description",
            price = BigDecimal("29.99"),
            categoryId = UUID.randomUUID(),
            active = true
        )

        val command = CreateOrderCommand(
            createdBy = userId,
            paymentMethod = PaymentMethod.CREDIT_CARD,
            products = listOf(
                ProductOrderRequest(
                    productId = productId,
                    quantity = BigDecimal("2")
                )
            )
        )

        // Mock do gateway para retornar o produto
        every { productGateway.getProductById(productId) } returns productResponse

        // Mock do repositório para retornar a ordem salva
        every { orderRepository.save(any()) } answers { firstArg() }

        // When
        val result = createOrderUseCase.execute(command)

        // Then
        assertNotNull(result)
        assertEquals(1, result.products.size)
        assertEquals(BigDecimal("59.98"), result.totalAmount) // 29.99 * 2
        assertEquals(PaymentMethod.CREDIT_CARD, result.paymentMethod)

        // Verifica que o gateway foi chamado
        verify(exactly = 1) { productGateway.getProductById(productId) }
        verify(exactly = 1) { orderRepository.save(any()) }
    }

    @Test
    fun `should throw exception when product not found`() {
        // Given
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val command = CreateOrderCommand(
            createdBy = userId,
            paymentMethod = PaymentMethod.DEBIT_CARD,
            products = listOf(
                ProductOrderRequest(
                    productId = productId,
                    quantity = BigDecimal("1")
                )
            )
        )

        // Mock do gateway para lançar exceção
        every { productGateway.getProductById(productId) } throws ProductNotFoundException(productId)

        // When & Then
        assertThrows<ProductNotFoundException> {
            createOrderUseCase.execute(command)
        }

        // Verifica que o repositório não foi chamado
        verify(exactly = 0) { orderRepository.save(any()) }
    }

    @Test
    fun `should throw exception when product is not active`() {
        // Given
        val productId = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val productResponse = ProductResponse(
            id = productId,
            name = "Inactive Product",
            description = "Test Description",
            price = BigDecimal("29.99"),
            categoryId = UUID.randomUUID(),
            active = false  // Produto inativo
        )

        val command = CreateOrderCommand(
            createdBy = userId,
            paymentMethod = PaymentMethod.PIX,
            products = listOf(
                ProductOrderRequest(
                    productId = productId,
                    quantity = BigDecimal("1")
                )
            )
        )

        // Mock do gateway
        every { productGateway.getProductById(productId) } returns productResponse

        // When & Then
        val exception = assertThrows<IllegalArgumentException> {
            createOrderUseCase.execute(command)
        }

        assertTrue(exception.message!!.contains("not active"))

        // Verifica que o repositório não foi chamado
        verify(exactly = 0) { orderRepository.save(any()) }
    }

    @Test
    fun `should create order with multiple products`() {
        // Given
        val product1Id = UUID.randomUUID()
        val product2Id = UUID.randomUUID()
        val userId = UUID.randomUUID()

        val product1 = ProductResponse(
            id = product1Id,
            name = "Product 1",
            description = "Description 1",
            price = BigDecimal("10.00"),
            categoryId = UUID.randomUUID(),
            active = true
        )

        val product2 = ProductResponse(
            id = product2Id,
            name = "Product 2",
            description = "Description 2",
            price = BigDecimal("20.00"),
            categoryId = UUID.randomUUID(),
            active = true
        )

        val command = CreateOrderCommand(
            createdBy = userId,
            paymentMethod = PaymentMethod.MONEY,
            products = listOf(
                ProductOrderRequest(product1Id, BigDecimal("3")),
                ProductOrderRequest(product2Id, BigDecimal("2"))
            )
        )

        // Mock dos gateways
        every { productGateway.getProductById(product1Id) } returns product1
        every { productGateway.getProductById(product2Id) } returns product2
        every { orderRepository.save(any()) } answers { firstArg() }

        // When
        val result = createOrderUseCase.execute(command)

        // Then
        assertEquals(2, result.products.size)
        assertEquals(BigDecimal("70.00"), result.totalAmount) // (10*3) + (20*2)

        verify(exactly = 1) { productGateway.getProductById(product1Id) }
        verify(exactly = 1) { productGateway.getProductById(product2Id) }
    }
}

