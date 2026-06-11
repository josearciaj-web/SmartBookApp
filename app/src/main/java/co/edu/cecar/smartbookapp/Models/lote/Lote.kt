package co.edu.cecar.smartbookapp.Models.lote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Lote(
    @SerialName("lote") val numerLote: Int
)
