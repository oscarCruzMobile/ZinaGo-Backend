package com.zinago.backend.features.users.domain.usecases

import com.zinago.backend.features.users.data.UserRepository
import com.zinago.backend.features.users.presentation.dto.UserResponse
import com.zinago.backend.features.users.presentation.dto.toResponse
import com.zinago.backend.shared.exception.ResourceNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Component
class ChangeNameUseCase(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(id: Long, newName: String): UserResponse {

        // 1. Verificar que existe
        if (!userRepository.existsById(id)) {
            throw ResourceNotFoundException("Usuario con ID $id no encontrado")
        }

        // 2. Actualizar nombre
        userRepository.updateName(
            id   = id,
            name = newName,
            now  = OffsetDateTime.now()
        )

        // 3. Devolver el usuario actualizado
        return userRepository.findById(id).get().toResponse()
    }
}