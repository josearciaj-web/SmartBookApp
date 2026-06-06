package co.edu.cecar.smartbookapp.Models.Venta

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class VentaResponse(
    @SerialName("id") val id: Int,
    @SerialName("numeroRecibo") val numeroRecibo: String,
    @SerialName("numeroComprobante") val numeroComprobante: String,
    @SerialName("total") val total: Double,
    @SerialName("fecha") val fecha: String,
    @SerialName("clienteNombre") val clienteNombre: String
)