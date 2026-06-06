package co.edu.cecar.smartbookapp.Models.Usuario

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    @SerialName("id") val id: Int? = null,
    @SerialName("nombres") val nombreUsuario: String = "",
    @SerialName("email") val correo: String = "",
    @SerialName("rol") val rol: String = "Vendedor",
    @SerialName("activo") val activo: Boolean = true
)
