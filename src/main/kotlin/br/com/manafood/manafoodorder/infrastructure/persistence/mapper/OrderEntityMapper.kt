package br.com.manafood.manafoodorder.infrastructure.persistence.mapper

import br.com.manafood.manafoodorder.domain.common.Paged
import br.com.manafood.manafoodorder.domain.model.Order
import br.com.manafood.manafoodorder.domain.valueobject.OrderStatus
import br.com.manafood.manafoodorder.domain.valueobject.PaymentMethod
import br.com.manafood.manafoodorder.infrastructure.persistence.entity.OrderJpaEntity
import org.springframework.data.domain.Page

object OrderEntityMapper {

    fun toEntity(domain: Order): OrderJpaEntity =
        OrderJpaEntity(
            id = domain.id,
            orderConfirmationTime = domain.orderConfirmationTime,
            paymentMethod = domain.paymentMethod.code,
            orderStatus = domain.orderStatus.code,
            totalAmount = domain.totalAmount,
            products = OrderProductEntityMapper.toEntityList(domain.products),
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            createdBy = domain.createdBy,
            updatedBy = domain.updatedBy,
            deleted = domain.deleted
        )

    fun toDomain(jpa: OrderJpaEntity): Order =
        Order(
            id = jpa.id,
            orderConfirmationTime = jpa.orderConfirmationTime,
            paymentMethod = PaymentMethod.fromCode(jpa.paymentMethod),
            orderStatus = OrderStatus.fromCode(jpa.orderStatus),
            totalAmount = jpa.totalAmount,
            products = OrderProductEntityMapper.toDomainList(jpa.products),
            createdAt = jpa.createdAt,
            updatedAt = jpa.updatedAt,
            createdBy = jpa.createdBy,
            updatedBy = jpa.updatedBy,
            deleted = jpa.deleted
        )

    fun toPagedDomain(jpa: Page<OrderJpaEntity>): Paged<Order> =
        Paged(
            items = jpa.content.map { toDomain(it) },
            page = jpa.number,
            pageSize = jpa.size,
            totalItems = jpa.totalElements,
            totalPages = jpa.totalPages
        )
}