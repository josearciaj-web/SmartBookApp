package co.edu.cecar.smartbookapp.Models.DashBoard

import kotlinx.serialization.Serializable

@Serializable
data class dashboardResponse(
    val totalClientes: Int,
    val totalLibros: Int,
    val totalingresos: Double,
    val ingresosMensuales: Double?=0.0
)
