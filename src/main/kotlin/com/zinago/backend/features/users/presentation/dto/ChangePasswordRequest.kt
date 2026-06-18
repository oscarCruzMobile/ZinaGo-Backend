package com.zinago.backend.features.users.presentation.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ChangePasswordRequest(

    @field:NotBlank(message = "La contraseña actual es obligatoria")
    val currentPassword: String,

    @field:NotBlank(message = "La nueva contraseña es obligatoria")
    @field:Size(min = 8, message = "La nueva contraseña debe tener al menos 8 caracteres")
    val newPassword: String,

    @field:NotBlank(message = "La confirmación es obligatoria")
    val confirmPassword: String
)