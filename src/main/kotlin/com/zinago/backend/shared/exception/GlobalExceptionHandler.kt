package com.zinago.backend.shared.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException): ResponseEntity<ErrorResponse> {
        return buildError(HttpStatus.NOT_FOUND, ex.message ?: "Recurso no encontrado")
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(ex: BusinessException): ResponseEntity<ErrorResponse> {
        return buildError(HttpStatus.CONFLICT, ex.message ?: "Conflicto de negocio")
    }

    @ExceptionHandler(BadCredentialsException::class)
    fun handleBadCredentials(ex: BadCredentialsException): ResponseEntity<ErrorResponse> {
        return buildError(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas")
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errors = ex.bindingResult.allErrors.associate { error ->
            val fieldName = (error as FieldError).field
            fieldName to (error.defaultMessage ?: "Valor inválido")
        }
        val response = ValidationErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            errors = errors,
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(response, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception): ResponseEntity<ErrorResponse> {
        ex.printStackTrace() // ← agrega esta línea temporal
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.message ?: "Ocurrió un error inesperado")
    }

    private fun buildError(status: HttpStatus, message: String): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            status = status.value(),
            error = status.reasonPhrase,
            message = message,
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity(error, status)
    }
}

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val timestamp: LocalDateTime
)

data class ValidationErrorResponse(
    val status: Int,
    val error: String,
    val errors: Map<String, String>,
    val timestamp: LocalDateTime
)