package co.edu.cecar.smartbookapp.Models.Clientes

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class BuscarCliente(
    @SerialName("identificacion") val identificacion: String = "",
    @SerialName("nombres") val nombres: String = "",
    @SerialName("email") val email: String = "",
    @SerialName("celular") val celular: String = "",
    @SerialName("fechaNacimiento") val fechaNacimiento: String = ""
)
