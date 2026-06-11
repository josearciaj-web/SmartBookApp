package co.edu.cecar.smartbookapp.Models.Dashboard // Ajusta el package a tu ruta real

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class dashboardResponse(
    @SerialName("totalLibros") val totalLibros: Int = 0,
    @SerialName("totalClientes") val totalClientes: Int = 0,
    @SerialName("cantVentasMes") val cantVentasMes: Int = 0,
    @SerialName("totalVentasMes") val totalVentasMes: Double = 0.0 )