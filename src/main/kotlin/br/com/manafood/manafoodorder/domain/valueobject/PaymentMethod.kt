package br.com.manafood.manafoodorder.domain.valueobject

enum class PaymentMethod(val code: Int) {
    QR_CODE(0);

    companion object {
        fun fromCode(code: Int): PaymentMethod =
            PaymentMethod.entries.find { it.code == code }
                ?: throw IllegalArgumentException("Método de pagamento com código $code não encontrado")
    }
}