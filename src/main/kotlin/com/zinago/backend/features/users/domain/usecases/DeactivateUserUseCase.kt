package com.zinago.backend.features.users.domain.usecases

import com.zinago.backend.features.users.data.UserRepository
import com.zinago.backend.features.users.presentation.dto.UserResponse
import com.zinago.backend.features.users.presentation.dto.toResponse
import com.zinago.backend.shared.exception.BusinessException
import com.zinago.backend.shared.exception.ResourceNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Component
class DeactivateUserUseCase(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(id: Long): UserResponse {

        val user = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Usuario con ID $id no encontrado")
        }

        // Evitar desactivar si ya está desactivado
        if (!user.lactive) {
            throw BusinessException("El usuario $id ya está desactivado")
        }

        userRepository.deactivateUser(
            id  = id,
            now = OffsetDateTime.now()
        )

        return userRepository.findById(id).get().toResponse()
    }
}