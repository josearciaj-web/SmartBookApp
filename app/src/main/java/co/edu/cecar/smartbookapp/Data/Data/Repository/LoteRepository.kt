package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Ingresos.lote
import co.edu.cecar.smartbookapp.Models.lote.LoteResponse
import co.edu.cecar.smartbookapp.Models.lote.Lote
import co.edu.cecar.smartbookapp.Models.RespuestaError.RespuestaError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class LoteRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL = "https://api.smartbooks.cecar.cloud/api/Lotes"
    private val jsonDecoder = Json { ignoreUnknownKeys = true }


    suspend fun obtenerLotes(): Result<List<LoteResponse>> = runCatching {
        val respuesta = client.get(BASE_URL)
        if (!respuesta.status.isSuccess()) {
            throw Exception("Error al traer los lotes del servidor")
        }

        val jsonTexto = respuesta.bodyAsText()
        jsonDecoder.decodeFromString<List<LoteResponse>>(jsonTexto)
    }

    suspend fun crearLote(lote: lote): Result<Unit> = runCatching {
        val respuesta = client.post(BASE_URL) {
            setBody(lote)
        }
        if (!respuesta.status.isSuccess()) {
            val errorCrudo = respuesta.bodyAsText()
            val mensajeServidor = try {
                val errorParseado = jsonDecoder.decodeFromString<RespuestaError>(errorCrudo)
                errorParseado.detail ?: "No se pudo registrar el lote."
            } catch (e: Exception) {
                "El lote ya existe o el código es inválido."
            }
            throw Exception(mensajeServidor)
        }
    }
}