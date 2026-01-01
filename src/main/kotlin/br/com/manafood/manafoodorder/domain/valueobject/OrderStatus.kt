package br.com.manafood.manafoodorder.domain.valueobject

enum class OrderStatus(val code: Int) {
    CREATED(1),
    RECEIVED(2),
    PREPARING(3),
    READY(4),
    FINISHED(5),
    REJECTED(6)
}