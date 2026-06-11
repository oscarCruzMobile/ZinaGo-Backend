package com.zinago.backend.config

import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component

@Component
class SecurityHeaderFilter : Filter {
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpResponse = response as HttpServletResponse

        // 1. Evita que el navegador intente "adivinar" el tipo de contenido (MIME Sniffing)
        httpResponse.setHeader("X-Content-Type-Options", "nosniff")

        // 2. Mitiga ataques de Clickjacking (impide que tu API se renderice en frames externos)
        httpResponse.setHeader("X-Frame-Options", "DENY")

        // 3. Activa la protección XSS integrada en navegadores antiguos
        httpResponse.setHeader("X-XSS-Protection", "1; mode=block")

        // 4. HSTS (Fuerza el uso de HTTPS) -> Actívalo solo cuando tengas certificado SSL en producción
        // httpResponse.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")

        chain.doFilter(request, response)
    }
}