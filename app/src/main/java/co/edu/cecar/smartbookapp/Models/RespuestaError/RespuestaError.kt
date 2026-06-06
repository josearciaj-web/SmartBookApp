package co.edu.cecar.smartbookapp.Models.RespuestaError


import kotlinx.serialization.Serializable

@Serializable
data class RespuestaError(
    val detail: String? = null
)