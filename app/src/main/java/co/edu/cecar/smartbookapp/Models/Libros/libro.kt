package co.edu.cecar.smartbookapp.Models.Libros

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Libro(
    @SerialName("id") val id: Int? = null,
    @SerialName("nombre") val nombre: String = "",
    @SerialName("nivel") val nivel: String = "",
    @SerialName("tipo") val tipo: Int = 0,
    @SerialName("edicion") val edicion: String = "",
    @SerialName("unidades") val unidades: Int = 0,
    @SerialName("lote") val lote: Int = 0,
    @SerialName("valorCompra") val valorCompra: Double = 0.0,
    @SerialName("valorVentaPublico") val valorVentaPublico: Double = 0.0
)
