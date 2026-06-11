package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Venta.CrearVentaRequest
import co.edu.cecar.smartbookapp.Models.Venta.VentaResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.builtins.ListSerializer

class VentaRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Ventas"
    private val jsonDecoder = Json { ignoreUnknownKeys = true }

    suspend fun registrarNuevaVenta(venta: CrearVentaRequest): Result<Unit> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(venta)
        }
        check(respuesta.status.isSuccess()) { "Error al registrar la venta: ${respuesta.status.value}" }
    }

    suspend fun obtenerHistorial(desde: String? = null, hasta: String? = null): Result<List<VentaResponse>> = runCatching {
        val respuesta = client.get(BASE_URL_API) {
            if (!desde.isNullOrBlank( )) parameter("desde", desde)
            if (!hasta.isNullOrBlank()) parameter("hasta", hasta)
        }
        check(respuesta.status.isSuccess()) { "Error al obtener historial: ${respuesta.status.value}" }

        val jsonTexto = respuesta.bodyAsText()
        jsonDecoder.decodeFromString(ListSerializer(VentaResponse.serializer()), jsonTexto)
    }

    suspend fun buscarVentaPorId(id: Int): Result<VentaResponse> = runCatching {
        val respuesta = client.get("$BASE_URL_API/$id")
        check(respuesta.status.isSuccess()) { "Error al buscar la venta: ${respuesta.status.value}" }

        val jsonTexto = respuesta.bodyAsText()
        jsonDecoder.decodeFromString(VentaResponse.serializer(), jsonTexto)
    }
}