package com.zinago.backend.features.users.presentation

import com.zinago.backend.features.users.domain.usecases.CreateUserUseCase
import com.zinago.backend.features.users.presentation.dto.CreateUserRequest
import com.zinago.backend.features.users.presentation.dto.UserResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/mobile/auth")
class MobileUserController(
    private val createUser:       CreateUserUseCase,

) {
    @PostMapping("/register")
    fun register(
        @Valid @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(createUser.execute(request))

    @PostMapping("/login")
    fun login(
        @Valid @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(createUser.execute(request))


}
