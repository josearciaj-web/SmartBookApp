package co.edu.cecar.smartbookapp.Models.lote


import kotlinx.serialization.Serializable

@Serializable
data class LoteResponse(
    val codigo: Int,
    val actual: Boolean
)