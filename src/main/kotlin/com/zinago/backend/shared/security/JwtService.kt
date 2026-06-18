package com.zinago.backend.shared.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey  // ✅ Este javax.crypto es del JDK, NO de Jakarta — se mantiene

@Service
class JwtService {

    @Value("\${app.jwt.secret}")
    private lateinit var secretKey: String

    @Value("\${app.jwt.expiration-ms:86400000}")
    private var jwtExpiration: Long = 86400000

    @Value("\${app.jwt.refresh-expiration-ms:604800000}")
    private var refreshExpiration: Long = 604800000

    fun extractEmail(token: String): String? =
        extractClaim(token) { it.subject }

    fun generateToken(userDetails: UserDetails): String =
        generateToken(emptyMap(), userDetails)

    fun generateToken(extraClaims: Map<String, Any>, userDetails: UserDetails): String =
        buildToken(extraClaims, userDetails, jwtExpiration)

    fun generateRefreshToken(userDetails: UserDetails): String =
        buildToken(emptyMap(), userDetails, refreshExpiration)

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token)
        return email == userDetails.username && !isTokenExpired(token)
    }

    private fun buildToken(
        extraClaims: Map<String, Any>,
        userDetails: UserDetails,
        expiration: Long
    ): String =
        Jwts.builder()
            .claims(extraClaims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact()

    private fun isTokenExpired(token: String): Boolean =
        extractExpiration(token).before(Date())

    private fun extractExpiration(token: String): Date =
        extractClaim(token) { it.expiration }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T =
        claimsResolver(extractAllClaims(token))

    private fun extractAllClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(getSigningKey())  // ✅ Sin cast — getSigningKey() ya retorna SecretKey
            .build()
            .parseSignedClaims(token)
            .payload

    // ✅ Retorna SecretKey directamente — Keys.hmacShaKeyFor() ya retorna SecretKey
    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}