package co.edu.cecar.smartbookapp.Models.Seguridad

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val token: String,
    val tipoToken: String? = "Bearer",
    val usuarioId: Int? = null,
    val nombres: String? = null,
    val rol: String? = null
)
