package com.zinago.backend.features.users.presentation.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class CreateUserRequest(

    @field:NotBlank(message = "El nombre es obligatorio")
    @field:Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    val name: String,

    @field:NotBlank(message = "El email es obligatorio")
    @field:Email(message = "El email no tiene un formato válido")
    val email: String,

    @field:NotBlank(message = "La contraseña es obligatoria")
    @field:Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    val password: String,

    // ID del rol a asignar (debe existir en tabla roles)
    @field:NotNull(message = "El rol es obligatorio")
    @field:Min(value = 1, message = "El roleId debe ser un ID válido")
    val roleId: Long
)