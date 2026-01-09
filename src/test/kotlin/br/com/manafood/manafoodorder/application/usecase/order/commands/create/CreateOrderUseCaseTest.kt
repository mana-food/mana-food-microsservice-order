package br.com.manafood.manafoodorder.application.usecase.order.commands.create

import br.com.manafood.manafoodorder.application.dto.ProductDTO
import br.com.manafood.manafoodorder.application.factory.OrderProductFactory
import br.com.manafood.manafoodorder.application.service.ProductValidationService
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.model.OrderProduct
import br.com.manafood.manafoodorder.domain.repository.OrderRepository
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

class CreateOrderUseCaseTest {

    private lateinit var orderRepository: OrderRepository
    private lateinit var productValidationService: ProductValidationService
    private lateinit var orderProductFactory: OrderProductFactory
    private lateinit var createOrderUseCase: CreateOrderUseCase

    @BeforeEach
    fun setUp() {
        orderRepository = mockk()
        productValidationService = mockk()
        orderProductFactory = mockk()
        createOrderUseCase = CreateOrderUseCase(
            orderRepository,
            productValidationService,
            orderProductFactory
        )
    }

    @Test
    fun `should create order successfully`() {
        // Given
        val createdBy = UUID.randomUUID()
        val productId = UUID.randomUUID()
        val productCommand = CreateOrderProductCommand(productId = productId, quantity = 2)
        val command = CreateOrderCommand(
            createdBy = createdBy,
            paymentMethod = 0,
            products = listOf(productCommand)
        )

        val productDTO = ProductDTO(
            id = productId,
            name = "Test Product",
            description = "Description",
            unitPrice = BigDecimal("50.00"),
            categoryId = UUID.randomUUID()
        )

        val orderProduct = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = UUID.randomUUID(),
            productId = productId,
            productName = "Test Product",
            unitPrice = BigDecimal("50.00"),
            quantity = 2
        )

        every { productValidationService.validateAndGetProduct(productId) } returns productDTO
        every { orderProductFactory.createOrderProduct(any(), any(), any(), any()) } returns orderProduct

        val orderSlot = slot<Order>()
        every { orderRepository.save(capture(orderSlot)) } answers { orderSlot.captured }

        // When
        val result = createOrderUseCase.execute(command)

        // Then
        assertNotNull(result)
        assertEquals(OrderStatus.CREATED, result.orderStatus)
        assertEquals(PaymentMethod.QR_CODE, result.paymentMethod)
        assertEquals(0, BigDecimal("100.00").compareTo(result.totalAmount))
        verify(exactly = 1) { productValidationService.validateAndGetProduct(productId) }
        verify(exactly = 1) { orderProductFactory.createOrderProduct(any(), any(), any(), any()) }
        verify(exactly = 1) { orderRepository.save(any()) }
    }

    @Test
    fun `should create order with multiple products`() {
        // Given
        val createdBy = UUID.randomUUID()
        val productId1 = UUID.randomUUID()
        val productId2 = UUID.randomUUID()

        val command = CreateOrderCommand(
            createdBy = createdBy,
            paymentMethod = 0,
            products = listOf(
                CreateOrderProductCommand(productId = productId1, quantity = 2),
                CreateOrderProductCommand(productId = productId2, quantity = 1)
            )
        )

        val productDTO1 = ProductDTO(
            id = productId1,
            name = "Product 1",
            description = "Description 1",
            unitPrice = BigDecimal("30.00"),
            categoryId = UUID.randomUUID()
        )

        val productDTO2 = ProductDTO(
            id = productId2,
            name = "Product 2",
            description = "Description 2",
            unitPrice = BigDecimal("40.00"),
            categoryId = UUID.randomUUID()
        )

        val orderProduct1 = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = UUID.randomUUID(),
            productId = productId1,
            productName = "Product 1",
            unitPrice = BigDecimal("30.00"),
            quantity = 2
        )

        val orderProduct2 = OrderProduct(
            id = UUID.randomUUID(),
            createdBy = createdBy,
            createdAt = LocalDateTime.now(),
            updatedBy = null,
            updatedAt = null,
            deleted = false,
            orderId = UUID.randomUUID(),
            productId = productId2,
            productName = "Product 2",
            unitPrice = BigDecimal("40.00"),
            quantity = 1
        )

        every { productValidationService.validateAndGetProduct(productId1) } returns productDTO1
        every { productValidationService.validateAndGetProduct(productId2) } returns productDTO2
        every { orderProductFactory.createOrderProduct(any(), productDTO1, any(), any()) } returns orderProduct1
        every { orderProductFactory.createOrderProduct(any(), productDTO2, any(), any()) } returns orderProduct2

        val orderSlot = slot<Order>()
        every { orderRepository.save(capture(orderSlot)) } answers { orderSlot.captured }

        // When
        val result = createOrderUseCase.execute(command)

        // Then
        assertNotNull(result)
        assertEquals(0, BigDecimal("100.00").compareTo(result.totalAmount))
        assertEquals(2, result.products.size)
        verify(exactly = 2) { productValidationService.validateAndGetProduct(any()) }
        verify(exactly = 2) { orderProductFactory.createOrderProduct(any(), any(), any(), any()) }
    }
}

