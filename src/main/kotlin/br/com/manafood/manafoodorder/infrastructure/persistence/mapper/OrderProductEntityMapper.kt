package br.com.manafood.manafoodorder.infrastructure.persistence.mapper

import br.com.manafood.manafoodorder.domain.model.OrderProduct
import br.com.manafood.manafoodorder.infrastructure.persistence.entity.OrderProductJpaEntity

import java.util.UUID

object OrderProductEntityMapper {

    fun toEntityList(domain: List<OrderProduct>): List<OrderProductJpaEntity> =
        domain.map { item ->
            OrderProductJpaEntity(
                id = UUID.randomUUID(),
                productId = item.productId,
                quantity = item.quantity,
                order = item.orderId, // TODO - resolver amanhã
                productName = item.productName,
                unitPrice = item.unitPrice,
                subtotal = item.subtotal,
                createdAt = item.createdAt,
                updatedAt = item.updatedAt,
                createdBy = item.createdBy,
                updatedBy = item.updatedBy,
                deleted = item.deleted
            )
        }

    fun toDomainList(jpas: List<OrderProductJpaEntity>): List<OrderProduct> =
        jpas.map { jpa ->
            OrderProduct(
                id = UUID.randomUUID(),
                productId = jpa.productId,
                quantity = jpa.quantity,
                orderId = jpa.order.id,
                productName = jpa.productName,
                unitPrice = jpa.unitPrice,
                subtotal = jpa.subtotal,
                createdAt = jpa.createdAt,
                updatedAt = jpa.updatedAt,
                createdBy = jpa.createdBy,
                updatedBy = jpa.updatedBy,
                deleted = jpa.deleted
            )
        }
}