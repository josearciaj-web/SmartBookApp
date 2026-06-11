package co.edu.cecar.smartbookapp.Models.Usuario

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    @SerialName("id") val id: Int? = null,
    @SerialName("identificacion") val identificacion: String = "",
    @SerialName("nombres") val nombreUsuario: String = "",
    @SerialName("email") val correo: String = "",
    @SerialName("password") val password: String = "",
    @SerialName("rol") val rol: String = "Vendedor",
    @SerialName("activo") val activo: Boolean = true
)