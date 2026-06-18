package com.zinago.backend.features.users.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.OffsetDateTime

@Repository
@Transactional(readOnly = true)
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

    // ✅ CORREGIDO: Cambiado u.password por u.passwordHash para coincidir con la entidad
    @Transactional
    @Modifying
    @Query("""
        UPDATE User u SET 
            u.passwordHash = :password,
            u.updatedAt    = :now
        WHERE u.id = :id
    """)
    fun updatePassword(
        @Param("id") id: Long,
        @Param("password") password: String, // Cambiado a String no null para hacer match exacto
        @Param("now") now: OffsetDateTime
    ): Int

    @Transactional
    @Modifying
    @Query("""
        UPDATE User u SET 
            u.name      = :name,
            u.updatedAt = :now
        WHERE u.id = :id
    """)
    fun updateName(
        @Param("id") id: Long,
        @Param("name") name: String,
        @Param("now") now: OffsetDateTime
    ): Int

    @Transactional
    @Modifying
    @Query("""
        UPDATE User u SET 
            u.lvalidated  = true,
            u.validatedAt = :now,
            u.updatedAt   = :now
        WHERE u.id = :id
    """)
    fun validateAccount(
        @Param("id") id: Long,
        @Param("now") now: OffsetDateTime
    ): Int

    @Transactional
    @Modifying
    @Query("""
        UPDATE User u SET 
            u.lactive   = false,
            u.updatedAt = :now
        WHERE u.id = :id
    """)
    fun deactivateUser(
        @Param("id") id: Long,
        @Param("now") now: OffsetDateTime
    ): Int
}