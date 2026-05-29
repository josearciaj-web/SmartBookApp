package co.edu.cecar.smartbookapp.Models.Ingresos

import kotlinx.serialization.Serializable

@Serializable
data class Ingreso(
    val id:Int?=null,
    val libroid: Int,
    val unidades: Int,
    val lote: Int,
    val valorCompra: Double,
    val valorVentaPublico: Double
)
