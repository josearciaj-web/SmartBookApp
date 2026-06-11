package co.edu.cecar.smartbookapp.Models.Ingresos

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName // Importante para interceptar el nombre del Swagger

@Serializable
data class Ingreso(
    @SerialName("id") val id: Int? = null,
    @SerialName("libroId") val libroid: Int = 0, // CORREGIDO: Mapea "libroId" del servidor y evita cierres inesperados
    @SerialName("unidades") val unidades: Int,
    @SerialName("lote") val lote: Int,
    @SerialName("valorCompra") val valorCompra: Double,
    @SerialName("valorVentaPublico") val valorVentaPublico: Double
)