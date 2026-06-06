package co.edu.cecar.smartbookapp.Models.Venta

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class CrearVentaRequest(
    @SerialName("identificacionCliente") val identificacionCliente: String,
    @SerialName("numeroComprobante") val numeroComprobante: String,
    @SerialName("observaciones") val observaciones: String,
    @SerialName("items") val items: List<ItemVentaRequest>
)

@Serializable
data class ItemVentaRequest(
    @SerialName("libroId") val libroId: Int,
    @SerialName("lote") val lote: Int,
    @SerialName("cantidad") val cantidad: Int
)