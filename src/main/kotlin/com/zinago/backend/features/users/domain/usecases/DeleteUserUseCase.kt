package com.zinago.backend.features.users.domain.usecases

import com.zinago.backend.features.users.data.UserRepository
import com.zinago.backend.shared.exception.ResourceNotFoundException
import org.springframework.stereotype.Component

@Component
class DeleteUserUseCase(
    private val userRepository: UserRepository
) {
    fun execute(id: Long) {
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("Usuario con ID $id no encontrado")
        }
        userRepository.deleteById(id)
    }
}