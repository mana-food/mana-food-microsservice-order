package br.com.manafood.manafoodorder.domain.valueobject

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class OrderStatusTest {

    @Test
    fun `should return CREATED for code 1`() {
        val status = OrderStatus.fromCode(1)
        assertEquals(OrderStatus.CREATED, status)
    }

    @Test
    fun `should return RECEIVED for code 2`() {
        val status = OrderStatus.fromCode(2)
        assertEquals(OrderStatus.RECEIVED, status)
    }

    @Test
    fun `should return PREPARING for code 3`() {
        val status = OrderStatus.fromCode(3)
        assertEquals(OrderStatus.PREPARING, status)
    }

    @Test
    fun `should return READY for code 4`() {
        val status = OrderStatus.fromCode(4)
        assertEquals(OrderStatus.READY, status)
    }

    @Test
    fun `should return FINISHED for code 5`() {
        val status = OrderStatus.fromCode(5)
        assertEquals(OrderStatus.FINISHED, status)
    }

    @Test
    fun `should return REJECTED for code 6`() {
        val status = OrderStatus.fromCode(6)
        assertEquals(OrderStatus.REJECTED, status)
    }

    @Test
    fun `should throw exception for invalid code`() {
        val exception = assertThrows<IllegalArgumentException> {
            OrderStatus.fromCode(999)
        }
        assertTrue(exception.message!!.contains("Status do pedido com código 999 não encontrado"))
    }

    @Test
    fun `should have correct code values`() {
        assertEquals(1, OrderStatus.CREATED.code)
        assertEquals(2, OrderStatus.RECEIVED.code)
        assertEquals(3, OrderStatus.PREPARING.code)
        assertEquals(4, OrderStatus.READY.code)
        assertEquals(5, OrderStatus.FINISHED.code)
        assertEquals(6, OrderStatus.REJECTED.code)
    }
}

