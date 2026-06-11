package com.zinago.backend.features.users

import com.zinago.backend.config.exception.ResourceNotFoundException
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getAllUsers(): List<UserResponse> {
        return userRepository.findAll().map { it.toResponse() }
    }

    // NUEVA FUNCIÓN: Busca por ID. Si es nulo, lanza la excepción controlada
    fun getUserById(id: Long): UserResponse {
        val user = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Usuario con el ID $id no fue encontrado")
        }
        return user.toResponse()
    }
}