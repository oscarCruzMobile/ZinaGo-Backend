package com.zinago.backend.features.users.presentation.dto

import com.zinago.backend.features.users.data.User
import java.time.OffsetDateTime

data class UserResponse(
    val id:          Long,
    val name:        String,
    val email:       String,
    val role:        String,
    val lactive:     Boolean,
    val lvalidated:  Boolean,
    val createdAt:   OffsetDateTime,
    val updatedAt:   OffsetDateTime,
    val validatedAt: OffsetDateTime?
    // passwordHash NUNCA se incluye en el response
)

fun User.toResponse(): UserResponse = UserResponse(
    id          = this.id ?: 0,
    name        = this.name,
    email       = this.email,
    role        = this.role.name,
    lactive     = this.lactive,
    lvalidated  = this.lvalidated,
    createdAt   = this.createdAt,
    updatedAt   = this.updatedAt,
    validatedAt = this.validatedAt
)