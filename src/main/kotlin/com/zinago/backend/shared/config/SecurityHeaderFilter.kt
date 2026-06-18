package com.zinago.backend.shared.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class SecurityHeaderFilter : Filter {

    @Value("\${app.security.hsts-enabled:false}")
    private var hstsEnabled: Boolean = false

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpResponse = response as HttpServletResponse

        // Evita MIME Sniffing
        httpResponse.setHeader("X-Content-Type-Options", "nosniff")

        // Evita Clickjacking
        httpResponse.setHeader("X-Frame-Options", "DENY")

        // Protección XSS en navegadores antiguos
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block")

        // Evita que el navegador filtre información del referrer
        httpResponse.setHeader("Referrer-Policy", "strict-origin-when-cross-origin")

        // Restringe acceso a APIs del navegador
        httpResponse.setHeader("Permissions-Policy", "camera=(), microphone=(), geolocation=()")

        // HSTS: solo activo en producción cuando hay SSL
        if (hstsEnabled) {
            httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
        }

        chain.doFilter(request, response)
    }
}