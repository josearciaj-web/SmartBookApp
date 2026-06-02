package co.edu.cecar.smartbookapp.Models.DashBoard

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class dashboardResponse(
    @SerialName("totalClientes") val totalClientes: Int = 0,
    @SerialName("totalLibros") val totalLibros: Int = 0,
    @SerialName("totalIngresos") val totalingresos: Double = 0.0,
    @SerialName("ingresosMensuales") val ingresosMensuales: Double? = 0.0
)
