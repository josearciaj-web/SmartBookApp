package co.edu.cecar.smartbookapp.Models.Usuario

import kotlinx.serialization.Serializable

@Serializable

data class Usuario(
    val id: Int? = null, // Nulo al hacer POST
    val nombreUsuario: String,
    val correo: String,
    val rol: String,      // Ej: "Admin", "Vendedor"
    val activo: Boolean
)
