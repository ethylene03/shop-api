package com.princess.shopapi.service

import com.princess.shopapi.dto.ProductDTO
import com.princess.shopapi.dto.Role
import com.princess.shopapi.helpers.ResourceNotFoundException
import com.princess.shopapi.helpers.buildSpecification
import com.princess.shopapi.helpers.createProductEntity
import com.princess.shopapi.helpers.toProductResponse
import com.princess.shopapi.model.ProductEntity
import com.princess.shopapi.repository.ProductRepository
import org.slf4j.LoggerFactory
import org.springframework.data.domain.*
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.util.*

@Service
class ProductService(private val repository: ProductRepository) {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun create(product: ProductDTO): ProductDTO {
        log.debug("Saving product..")
        return product.createProductEntity()
            .let { repository.save(it) }
            .toProductResponse()
    }

    fun findAll(role: Role, userId: UUID, pageable: Pageable): Page<ProductDTO> {
        log.debug("Finding all products..")
        val page = when (role) {
            Role.BUYER -> repository.findAll(pageable)
            Role.SELLER -> repository.findAllByCreatedBy(userId, pageable)
        }

        log.debug("Finding products..")
        val page = repository.findAll(spec, customPageable)

        log.debug("Returning list..")
        val list = page.content.map { it.toProductResponse() }
        return PageImpl(
            list,
            page.pageable,
            page.totalElements
        )
    }

    fun find(id: UUID): ProductDTO {
        log.debug("Finding product..")
        return repository.findByIdAndIsDeletedFalse(id).orElseThrow {
            log.error("Data with id $id not found.")
            throw ResourceNotFoundException("ID does not exist.")
        }.toProductResponse()
    }

    fun update(id: UUID, details: ProductDTO): ProductDTO {
        log.debug("Finding product by given ID..")
        val currentProduct = repository.findByIdAndIsDeletedFalse(id).orElseThrow {
            log.error("Product not found.")
            ResourceNotFoundException("Product not found.")
        }

        log.debug("Updating product..")
        return currentProduct.apply {
            name = details.name
            description = details.description ?: ""
            price = details.price
            quantity = details.quantity
        }.let { repository.save(it) }.toProductResponse()
    }

    fun addQuantity(id: UUID, quantity: Int): ProductDTO {
        log.debug("Finding product by given ID..")
        val currentProduct = repository.findByIdAndIsDeletedFalse(id).orElseThrow {
            log.error("Product not found.")
            ResourceNotFoundException("Product not found.")
        }

        log.debug("Adding quantity..")
        return currentProduct.apply {
            this.quantity += quantity
        }.let { repository.save(it) }.toProductResponse()
    }

    fun delete(id: UUID) {
        log.debug("Checking if ID exists..")
        val product = repository.findByIdAndIsDeletedFalse(id).orElseThrow()

        log.debug("Deleting data..")
        product.apply { isDeleted = true }.let { repository.save(it) }
    }
}