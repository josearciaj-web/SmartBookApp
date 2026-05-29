package co.edu.cecar.smartbookapp.Models.Inventario

import kotlinx.serialization.Serializable

@Serializable
data class Inventario(
    val id: Int,
    val libroId: Int,
    val unidades: Int,
    val lote: Int,
    val valorCompra: Double,
    val valorVentaPublico: Double
)
