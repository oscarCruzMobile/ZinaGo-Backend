package com.zinago.backend.features.users.domain.usecases

import com.zinago.backend.features.users.data.UserRepository
import com.zinago.backend.features.users.presentation.dto.UserResponse
import com.zinago.backend.features.users.presentation.dto.toResponse
import org.springframework.stereotype.Component

@Component
class GetAllUsersUseCase(
    private val userRepository: UserRepository
) {
    fun execute(): List<UserResponse> =
        userRepository.findAll().map { it.toResponse() }
}