package co.edu.cecar.smartbookapp.Models.Clientes

import kotlinx.serialization.Serializable

@Serializable
data class CrearCliente(
    val identificacion: String,
    val nombres: String,
    val email: String,
    val celular: String,
    val fechaNacimiento: String
)
