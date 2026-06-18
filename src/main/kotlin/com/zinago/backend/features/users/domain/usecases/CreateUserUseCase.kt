package com.zinago.backend.features.users.domain.usecases

import com.zinago.backend.features.roles.data.RoleRepository
import com.zinago.backend.features.users.data.User
import com.zinago.backend.features.users.data.UserRepository
import com.zinago.backend.features.users.presentation.dto.CreateUserRequest
import com.zinago.backend.features.users.presentation.dto.UserResponse
import com.zinago.backend.features.users.presentation.dto.toResponse
import com.zinago.backend.shared.exception.BusinessException
import com.zinago.backend.shared.exception.ResourceNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class CreateUserUseCase(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun execute(request: CreateUserRequest): UserResponse {

        // 1. Verificar email único
        if (userRepository.existsByEmail(request.email)) {
            throw BusinessException("El email '${request.email}' ya está registrado")
        }

        // 2. Verificar que el rol existe
        val role = roleRepository.findById(request.roleId).orElseThrow {
            ResourceNotFoundException("Rol con ID ${request.roleId} no encontrado")
        }

        // 3. Construir usuario — passwordHash en lugar de password
        val user = User(
            name = request.name,
            email = request.email,
            passwordHash = passwordEncoder.encode(request.password) as String,
            role = role,
            lactive = true,
            lvalidated = false
        )

        return userRepository.save(user).toResponse()
    }
}