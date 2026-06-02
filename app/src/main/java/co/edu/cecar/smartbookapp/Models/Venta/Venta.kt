package co.edu.cecar.smartbookapp.Models.Venta

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class DetalleVenta(
    @SerialName("libroId") val libroId: Int = 0,
    @SerialName("cantidad") val cantidad: Int = 0,
    @SerialName("precioUnitario") val precioUnitario: Double = 0.0
)

@Serializable
data class Venta(
    @SerialName("id") val id: Int? = null,
    @SerialName("clienteId") val clienteId: String = "",
    @SerialName("usuarioId") val usuarioId: Int = 0,
    @SerialName("fecha") val fecha: String = "",
    @SerialName("total") val total: Double = 0.0,
    @SerialName("detalles") val detalles: List<DetalleVenta> = emptyList()
)
