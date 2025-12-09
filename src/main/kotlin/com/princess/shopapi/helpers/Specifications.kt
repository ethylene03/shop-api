package com.princess.shopapi.helpers

import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

fun <T> buildSpecification(value: String?): Specification<T> {
    return Specification { root, _, cb ->
        val predicates = mutableListOf<Predicate>()

        val search = "%${value?.lowercase()}%"

        if(!value.isNullOrBlank()) {
            val name = cb.like(cb.lower(root.get("name")), search)
            val description = cb.like(cb.lower(root.get("description")), search)
            predicates.add(cb.or(name, description))
        }

        cb.and(*predicates.toTypedArray())
    }
}