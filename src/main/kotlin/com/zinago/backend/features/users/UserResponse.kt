package com.zinago.backend.features.users


// Este objeto solo contiene los campos seguros para la red
data class UserResponse(
    val id: Long,
    val name: String,
    val email: String
)

// Función de extensión al estilo Kotlin para mapear fácilmente de la Entidad al DTO
fun User.toResponse(): UserResponse {
    return UserResponse(
        id = this.id ?: 0,
        name = this.name,
        email = this.email
    )
}