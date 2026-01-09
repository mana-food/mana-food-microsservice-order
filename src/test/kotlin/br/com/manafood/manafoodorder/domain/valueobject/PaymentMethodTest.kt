package br.com.manafood.manafoodorder.domain.valueobject

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PaymentMethodTest {

    @Test
    fun `should return QR_CODE for code 0`() {
        val paymentMethod = PaymentMethod.fromCode(0)
        assertEquals(PaymentMethod.QR_CODE, paymentMethod)
    }

    @Test
    fun `should throw exception for invalid code`() {
        val exception = assertThrows<IllegalArgumentException> {
            PaymentMethod.fromCode(999)
        }
        assertTrue(exception.message!!.contains("Método de pagamento com código 999 não encontrado"))
    }

    @Test
    fun `should have correct code value for QR_CODE`() {
        assertEquals(0, PaymentMethod.QR_CODE.code)
    }
}

