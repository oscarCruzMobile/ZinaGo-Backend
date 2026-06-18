package com.zinago.backend.features.users.domain.usecases

import com.zinago.backend.features.users.data.UserRepository
import com.zinago.backend.shared.exception.BusinessException
import com.zinago.backend.shared.exception.ResourceNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Component
class ChangePasswordUseCase(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun execute(id: Long, currentPassword: String, newPassword: String, confirmPassword: String) {

        val user = userRepository.findById(id).orElseThrow {
            ResourceNotFoundException("Usuario con ID $id no encontrado")
        }

        // Usa passwordHash (no .password) para evitar colisión con UserDetails
        if (!passwordEncoder.matches(currentPassword, user.passwordHash)) {
            throw BusinessException("La contraseña actual es incorrecta")
        }

        if (newPassword != confirmPassword) {
            throw BusinessException("La nueva contraseña y su confirmación no coinciden")
        }

        if (passwordEncoder.matches(newPassword, user.passwordHash)) {
            throw BusinessException("La nueva contraseña debe ser diferente a la actual")
        }

        userRepository.updatePassword(
            id = id,
            password = passwordEncoder.encode(newPassword) as String,
            now = OffsetDateTime.now()
        )
    }
}