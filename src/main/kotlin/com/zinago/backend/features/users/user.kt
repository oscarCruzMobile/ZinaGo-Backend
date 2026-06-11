package com.zinago.backend.features.users

import jakarta.persistence.*

@Entity
@Table(name = "users", schema = "public")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false, unique = true, length = 150)
    val email: String,

    @Column(nullable = false, length = 255)
    val password: String
)