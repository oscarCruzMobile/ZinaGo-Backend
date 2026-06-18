package com.zinago.backend

import com.zinago.backend.shared.utils.AppLogger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

// ✅ Punto de entrada principal — esto genera BackendApplicationKt
@SpringBootApplication
class BackendApplication

fun main(args: Array<String>) {
	runApplication<BackendApplication>(*args)
}

// ✅ Listener separado dentro del mismo archivo (o puedes moverlo a otro archivo)
@Component
class ApplicationReadyListener(private val environment: Environment) {

	@EventListener(ApplicationReadyEvent::class)
	fun onApplicationReady() {
		val port = environment.getProperty("local.server.port") ?: "8080"
		val contextPath = environment.getProperty("server.servlet.context-path") ?: ""

		AppLogger.success("🚀 Aplicación ZinaGo corriendo exitosamente!")
		AppLogger.log("Localhost URL: http://localhost:$port$contextPath")
	}
}