package com.permission.api.security

import org.springframework.stereotype.Component
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import com.auth0.jwt.JWT
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.TokenExpiredException

@Component
class AuthenticationFilter(
    @Value(value = "\${auth.auth0.apiAudience}")
    private val apiAudience: String,
    @Value(value = "\${auth.auth0.issuer}")
    private val issuer: String,
    @Value(value = "\${auth.apiKeys}")
    private val apiKeys: List<String>
) : OncePerRequestFilter() {
    @Throws(JWTVerificationException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain): Unit {
        val authHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        authenticateFromHeader(authHeader)

        filterChain.doFilter(request, response)
    }

    private fun authenticateFromHeader(authHeader: String): Unit {
        if (!authHeader.isEmpty()) {
            val authHeaderParts: List<String> = authHeader.split(" ")

            val authHeaderPrefix: String = authHeaderParts.first().lowercase()

            when (authHeaderPrefix) {
                "apikey" -> {
                    val apiKey: String = authHeaderParts.last()

                    validateAPIKey(apiKey)
                }
                "bearer" -> {
                    val token: String = authHeaderParts.last()

                    var decodedJWT: DecodedJWT

                    try {
                        decodedJWT = JWT.decode(token)
                    } catch (e: Exception) {
                        throw JWTDecodeException("Unable to verify JWT")
                    }

                    validateJWT(decodedJWT)

                    // SecurityContextHolder.getContext().authentication
                }
                else -> {
                    throw JWTVerificationException("No useful Authorization data provided by header")
                }
            }
        } else {
            throw JWTVerificationException("Request is missing Authentication")
        }
    }

    private fun validateJWT(jwt: DecodedJWT): Unit {
        val now: Long = System.currentTimeMillis()

        if (jwt.expiresAt?.toInstant()?.toEpochMilli() ?: now < now) {
            throw TokenExpiredException("JWT is expired")
        }

        if (jwt.issuer != issuer) {
            throw JWTVerificationException("JWT does not contain valid issuer")
        }

        if (!(jwt.audience?.contains(apiAudience) ?: false)) {
            throw JWTVerificationException("JWT does not contain valid audience")
        }
    }

    private fun validateAPIKey(apiKey: String): Unit {
        if (!apiKeys.contains(apiKey)) {
            throw JWTVerificationException("API Key does match any valid API Keys")
        }
    }
}
