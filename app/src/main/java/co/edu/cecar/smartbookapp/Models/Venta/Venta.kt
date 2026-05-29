package co.edu.cecar.smartbookapp.Models.Venta
import kotlinx.serialization.Serializable

@Serializable
data class DetalleVenta(
    val libroId: Int,
    val cantidad: Int,
    val precioUnitario: Double
)

@Serializable
data class Venta(
    val id: Int? = null,
    val clienteId: String,
    val usuarioId: Int,
    val fecha: String,
    val total: Double,
    val detalles: List<DetalleVenta>
)