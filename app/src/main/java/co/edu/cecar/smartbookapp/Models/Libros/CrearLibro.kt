package co.edu.cecar.smartbookapp.Models.Libros

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrearLibro(
    @SerialName("nombre") val nombre: String,
    @SerialName("nivel") val nivel: String,
    @SerialName("tipo") val tipo: Int,
    @SerialName("edicion") val edicion: String,
    @SerialName("unidades") val unidades: Int,
    @SerialName("lote") val lote: Int,
    @SerialName("valorCompra") val valorCompra: Double, // Ortografía correcta para el POST
    @SerialName("valorVentaPublico") val valorVentaPublico: Double // Ortografía correcta para el POST
)
