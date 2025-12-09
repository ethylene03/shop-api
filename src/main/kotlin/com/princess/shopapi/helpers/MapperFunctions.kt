package com.princess.shopapi.helpers

import com.princess.shopapi.dto.*
import com.princess.shopapi.model.*

fun UserEntity.toUserResponse(): UserDTO = UserDTO(
    id = this.id ?: throw IllegalArgumentException("User ID is required."),
    name = this.name,
    username = this.username,
    role = this.role,
    cart = this.cart?.toCartResponse() ?: throw IllegalArgumentException("Cart is required."),
)

fun UserDTO.createUserEntity(cart: CartEntity? = null): UserEntity = UserEntity(
    name = this.name,
    role = this.role,
    username = this.username,
    password = this.password ?: throw kotlin.IllegalArgumentException("Password is required."),
    cart = cart,
)

fun ProductEntity.toProductResponse(): ProductDTO = ProductDTO(
    id = this.id ?: throw IllegalArgumentException("Product ID is required."),
    name = this.name,
    price = this.price,
    description = this.description,
    quantity = this.quantity,
    seller = this.createdBy,
    isDeleted = this.isDeleted
)

fun ProductDTO.createProductEntity(): ProductEntity = ProductEntity(
    name = this.name,
    price = this.price,
    description = this.description ?: "",
    quantity = this.quantity
)

fun CartEntity.toCartResponse(): CartDTO = CartDTO(
    id = this.id,
    userId = this.user?.id ?: throw IllegalArgumentException("User ID is required."),
    totalAmount = this.totalAmount,
    products = this.items.sortedBy { it.createdAt }.map { it.toCartItemResponse() }.toMutableList()
)

fun CartDTO.createCartEntity(user: UserEntity): CartEntity = CartEntity(
    user = user,
    totalAmount = this.totalAmount,
    items = mutableListOf()
)

fun CartItemEntity.toCartItemResponse(): CartItemDTO = CartItemDTO(
    id = this.id,
    cartId = this.cart?.id ?: throw IllegalArgumentException("Cart ID is required."),
    product = this.product?.toProductResponse() ?: throw IllegalArgumentException("Product details is required."),
    quantity = this.quantity
)

fun CartItemDTO.createCartItemEntity(product: ProductEntity, cart: CartEntity): CartItemEntity = CartItemEntity(
    product = product,
    cart = cart,
    quantity = this.quantity
)

fun OrderEntity.toOrderResponse(): OrderDTO = OrderDTO(
    id = this.id,
    buyerId = this.buyer?.id ?: throw IllegalArgumentException("User ID is required."),
    sellerId = this.seller?.id ?: throw IllegalArgumentException("User ID is required."),
    status = this.status,
    totalAmount = this.totalAmount,
    products = this.items.map { it.toOrderItemResponse() }.toMutableList(),
    orderDate = this.createdAt
)

fun OrderDTO.createOrderEntity(buyer: UserEntity, seller: UserEntity): OrderEntity = OrderEntity(
    buyer = buyer,
    seller = seller,
    status = this.status,
    totalAmount = this.totalAmount,
    items = mutableListOf()
)

fun OrderItemEntity.toOrderItemResponse(): OrderItemDTO = OrderItemDTO(
    id = this.id,
    orderId = this.order?.id ?: throw IllegalArgumentException("Order ID is required."),
    product = this.product?.toProductResponse() ?: throw IllegalArgumentException("Product details is required."),
    quantity = this.quantity,
    priceAtPurchase = this.priceAtPurchase
)

fun OrderItemDTO.createOrderItemEntity(product: ProductEntity, order: OrderEntity): OrderItemEntity = OrderItemEntity(
    product = product,
    order = order,
    quantity = this.quantity,
    priceAtPurchase = this.priceAtPurchase
)