package com.zinago.backend.shared.utils

import org.slf4j.LoggerFactory

object AppLogger {
    // El Logger interno de SLF4J (usa el nombre del proyecto o módulo)
    private val rootLogger = LoggerFactory.getLogger("ZinaGoApp")

    // --- Definición de Paleta de Colores (Códigos ANSI) ---
    private const val ANSI_RESET = "\u001B[0m"
    private const val ANSI_GREEN = "\u001B[32m"   // SUCCESS
    private const val ANSI_CYAN  = "\u001B[36m"   // LOG (Info estándar)
    private const val ANSI_YELLOW = "\u001B[33m"  // WARNING
    private const val ANSI_RED    = "\u001B[31m"  // ERROR

    /**
     * Verde para operaciones exitosas (ej. Base de datos conectada, login correcto, etc.)
     */
    fun success(msg: String) {
        rootLogger.info("$ANSI_GREEN[SUCCESS] $msg$ANSI_RESET")
    }

    /**
     * Cian / Azul claro para flujos de información ordinaria de la app.
     */
    fun log(msg: String) {
        rootLogger.info("$ANSI_CYAN[LOG] $msg$ANSI_RESET")
    }

    /**
     * Amarillo para advertencias (ej. tiempo de respuesta lento, token cerca de expirar).
     */
    fun warning(msg: String) {
        rootLogger.warn("$ANSI_YELLOW[WARNING] $msg$ANSI_RESET")
    }

    /**
     * Rojo para excepciones o fallos del sistema.
     */
    fun error(msg: String, t: Throwable? = null) {
        if (t != null) {
            rootLogger.error("$ANSI_RED[ERROR] $msg$ANSI_RESET", t)
        } else {
            rootLogger.error("$ANSI_RED[ERROR] $msg$ANSI_RESET")
        }
    }
}