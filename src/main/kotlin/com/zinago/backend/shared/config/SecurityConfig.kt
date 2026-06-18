package com.zinago.backend.shared.config

import com.zinago.backend.shared.security.JwtAuthFilter
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
    private val authenticationProvider: AuthenticationProvider,
    private val securityHeaderFilter: SecurityHeaderFilter
) {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .cors { }
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                    .requestMatchers(
                        "/api/v1/mobile/auth/**",
                        "/actuator/health"
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)

            // 1. Primero registramos jwtAuthFilter apuntando al filtro nativo de Spring.
            // Con esto, la cadena ya conoce el orden exacto de JwtAuthFilter.
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)

            // 2. Ahora que JwtAuthFilter ya está registrado con su respectiva posición,
            // ya podemos meter securityHeaderFilter justo antes de él sin romper nada.
            .addFilterBefore(securityHeaderFilter, JwtAuthFilter::class.java)

        return http.build()
    }

    @Bean
    fun disableSecurityHeaderFilterInTomcat(filter: SecurityHeaderFilter): FilterRegistrationBean<SecurityHeaderFilter> {
        val registration = FilterRegistrationBean(filter)
        registration.isEnabled = false
        return registration
    }
}