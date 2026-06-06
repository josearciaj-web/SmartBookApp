package co.edu.cecar.smartbookapp.Models.Inventario

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Inventario(
    @SerialName("id") val id: Int? = null,
    @SerialName("libroId") val libroId: Int = 0,
    // Mapeamos 'stockDisponible' directamente a 'unidades' para que tu tabla y la suma de stock funcionen al instante
    @SerialName("stockDisponible") val unidades: Int = 0,
    @SerialName("lote") val lote: Int = 0,
    @SerialName("valorCompra") val valorCompra: Double = 0.0,
    @SerialName("valorVentaPublico") val valorVentaPublico: Double = 0.0,
    @SerialName("nombreLibro") val nombreLibro: String = "Desconocido",

    // Añadimos los demás campos que vienen en tu JSON por si los llegas a necesitar después
    @SerialName("nivelLibro") val nivelLibro: String = "",
    @SerialName("edicionLibro") val edicionLibro: String = "",
    @SerialName("tipoLibro") val tipoLibro: String = "",
    @SerialName("cantidadIngresada") val cantidadIngresada: Int = 0,
    @SerialName("cantidadVendida") val cantidadVendida: Int = 0
)