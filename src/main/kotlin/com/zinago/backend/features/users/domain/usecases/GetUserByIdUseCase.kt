package com.zinago.backend.features.users.domain.usecases

import com.zinago.backend.features.users.data.UserRepository
import com.zinago.backend.features.users.presentation.dto.UserResponse
import com.zinago.backend.features.users.presentation.dto.toResponse
import com.zinago.backend.shared.exception.ResourceNotFoundException
import org.springframework.stereotype.Component

@Component
class GetUserByIdUseCase(
    private val userRepository: UserRepository
) {
    fun execute(id: Long): UserResponse {
        val user = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Usuario con ID $id no encontrado")
        }
        return user.toResponse()
    }
}