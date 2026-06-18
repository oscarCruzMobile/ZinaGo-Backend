package com.zinago.backend.features.users.presentation

import com.zinago.backend.features.users.domain.usecases.*
import com.zinago.backend.features.users.presentation.dto.*
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val getAllUsers:       GetAllUsersUseCase,
    private val getUserById:      GetUserByIdUseCase,
    private val createUser:       CreateUserUseCase,
    private val changePassword:   ChangePasswordUseCase,
    private val changeName:       ChangeNameUseCase,
    private val validateAccount:  ValidateAccountUseCase,
    private val deactivateUser:   DeactivateUserUseCase,
    private val deleteUser:       DeleteUserUseCase
) {

    // ─────────────────────────────────────────
    // GET
    // ─────────────────────────────────────────

    /** Lista todos los usuarios — solo ADMIN */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun getAll(): ResponseEntity<List<UserResponse>> =
        ResponseEntity.ok(getAllUsers.execute())

    /** Obtiene un usuario por ID — JWT válido (cualquier rol) */
    @GetMapping("/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<UserResponse> =
        ResponseEntity.ok(getUserById.execute(id))

    // ─────────────────────────────────────────
    // POST
    // ─────────────────────────────────────────

    /** Crea un usuario nuevo — solo ADMIN */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    fun create(
        @Valid @RequestBody request: CreateUserRequest
    ): ResponseEntity<UserResponse> =
        ResponseEntity.status(HttpStatus.CREATED).body(createUser.execute(request))

    // ─────────────────────────────────────────
    // PATCH — operaciones de update independientes
    // ─────────────────────────────────────────

    /** Cambia el nombre del usuario */
    @PatchMapping("/{id}/name")
    fun changeName(
        @PathVariable id: Long,
        @Valid @RequestBody request: ChangeNameRequest
    ): ResponseEntity<UserResponse> =
        ResponseEntity.ok(changeName.execute(id, request.name))

    /** Cambia la contraseña — verifica la actual antes de cambiar */
    @PatchMapping("/{id}/password")
    fun changePassword(
        @PathVariable id: Long,
        @Valid @RequestBody request: ChangePasswordRequest
    ): ResponseEntity<Void> {
        changePassword.execute(
            id              = id,
            currentPassword = request.currentPassword,
            newPassword     = request.newPassword,
            confirmPassword = request.confirmPassword
        )
        return ResponseEntity.noContent().build()     // 204: OK sin body
    }

    /** Valida la cuenta del usuario — activa lvalidated y registra validatedAt */
    @PatchMapping("/{id}/validate")
    @PreAuthorize("hasRole('ADMIN')")
    fun validateAccount(
        @PathVariable id: Long
    ): ResponseEntity<UserResponse> =
        ResponseEntity.ok(validateAccount.execute(id))

    /** Desactiva al usuario — pone lactive = false */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    fun deactivate(
        @PathVariable id: Long
    ): ResponseEntity<UserResponse> =
        ResponseEntity.ok(deactivateUser.execute(id))

    // ─────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────

    /** Elimina físicamente el usuario — solo ADMIN */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        deleteUser.execute(id)
        return ResponseEntity.noContent().build()
    }
}