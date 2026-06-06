package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Venta.CrearVentaRequest
import co.edu.cecar.smartbookapp.Models.Venta.VentaResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class VentaRepository {

    private val client = HttpClientProvider.client
    // URL exacta según el servidor para el módulo de Ventas
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Ventas"

    // 1. Registrar una nueva venta (POST) utilizando el DTO de creación
    suspend fun registrarNuevaVenta(ventaRequest: CrearVentaRequest): Result<VentaResponse> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(ventaRequest)
        }
        if (!respuesta.status.isSuccess()) {
            throw Exception("Error al registrar venta: ${respuesta.status.value}")
        }
        respuesta.body<VentaResponse>()
    }

    // 2. Obtener el historial completo de ventas (GET)
    suspend fun obtenerHistorial(): Result<List<VentaResponse>> = runCatching {
        val respuesta = client.get(BASE_URL_API)
        if (!respuesta.status.isSuccess()) {
            throw Exception("Error al obtener el historial: ${respuesta.status.value}")
        }
        respuesta.body<List<VentaResponse>>()
    }

    // 3. Buscar una venta específica por su ID único (GET /api/Ventas/{id})
    suspend fun buscarVentaPorId(id: Int): Result<VentaResponse> = runCatching {
        val respuesta = client.get("${BASE_URL_API}/$id")
        if (!respuesta.status.isSuccess()) {
            throw Exception("Venta con ID $id no encontrada")
        }
        respuesta.body<VentaResponse>()
    }
}