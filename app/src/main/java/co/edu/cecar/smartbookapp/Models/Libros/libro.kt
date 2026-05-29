package co.edu.cecar.smartbookapp.Models.Libros

import kotlinx.serialization.Serializable

@Serializable
data class Libro(
    val id: Int? = null,
    val nombre: String,
    val nivel: String,
    val tipo: Int,
    val edicion: String,
    val unidades: Int,
    val lote: Int,
    val valorCompra: Double,
    val valorVentaPublico: Double
)
