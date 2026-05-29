package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Venta.Venta
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class VentaRepository {

    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Ventas"

    // 1. PROCESAR / FACTURAR UNA VENTA (POST)
    // Envía el encabezado junto con todos los libros del carrito de compras
    suspend fun realizarVenta(venta: Venta): Result<Venta> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(venta)
        }
        check(respuesta.status.isSuccess()) { "Error al procesar la venta: ${respuesta.status.value}" }
        respuesta.body<Venta>()
    }

    // 2. HISTORIAL DE VENTAS (GET)
    suspend fun obtenerHistorial(): Result<List<Venta>> = runCatching {
        val respuesta = client.get(BASE_URL_API)
        check(respuesta.status.isSuccess()) { "Error al traer historial: ${respuesta.status.value}" }
        respuesta.body<List<Venta>>()
    }

    // 3. VER DETALLE DE UNA FACTURA ESPECÍFICA (GET /api/Ventas/{id})
    suspend fun buscarVentaPorId(idQuery: Int): Result<Venta> = runCatching {
        val respuesta = client.get("${BASE_URL_API}/$idQuery")
        check(respuesta.status.isSuccess()) { "Error al buscar la factura: ${respuesta.status.value}" }
        respuesta.body<Venta>()
    }
}