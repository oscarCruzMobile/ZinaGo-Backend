package com.zinago.backend.features.users.data

import com.zinago.backend.features.roles.data.Role
import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.OffsetDateTime

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false, unique = true, length = 150)
    val email: String,

    // ✅ FIX: String? → String (elimina ambos errores de compilación)
    @Column(name = "password", nullable = false, length = 255)
    val passwordHash: String,

    @Column(nullable = false)
    val lactive: Boolean = true,

    @Column(nullable = false)
    val lvalidated: Boolean = false,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    val role: Role,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    val updatedAt: OffsetDateTime = OffsetDateTime.now(),

    @Column(name = "validated_at")
    val validatedAt: OffsetDateTime? = null

) : UserDetails {

    // ✅ Línea 50: compila sin error porque passwordHash ya es String
    override fun getPassword(): String = passwordHash

    override fun getUsername(): String = email

    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${role.name}"))

    override fun isAccountNonLocked(): Boolean = lactive
    override fun isEnabled(): Boolean = lactive && lvalidated
    override fun isAccountNonExpired(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true

    // ✅ Línea 72: compila sin error porque this.passwordHash ya es String
    fun copy(
        id:           Long?           = this.id,
        name:         String          = this.name,
        email:        String          = this.email,
        passwordHash: String          = this.passwordHash,
        lactive:      Boolean         = this.lactive,
        lvalidated:   Boolean         = this.lvalidated,
        role:         Role            = this.role,
        createdAt:    OffsetDateTime  = this.createdAt,
        updatedAt:    OffsetDateTime  = this.updatedAt,
        validatedAt:  OffsetDateTime? = this.validatedAt
    ) = User(id, name, email, passwordHash, lactive, lvalidated, role, createdAt, updatedAt, validatedAt)
}