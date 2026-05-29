package co.edu.cecar.smartbookapp.Models.Clientes

import kotlinx.serialization.Serializable

@Serializable
data class ActualizarCliente(
    val nombres: String,
    val email: String,
    val celular: String,
    val fechaNacimiento: String
)