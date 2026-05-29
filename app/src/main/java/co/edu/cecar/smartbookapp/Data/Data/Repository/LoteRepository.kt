package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.lote.Lote
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class LoteRepository {
    private val client = HttpClientProvider.client

    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Lotes"

    suspend fun obtenerLotes(): Result<List<Lote>> = runCatching {
        val respuesta = client.get(BASE_URL_API)
        check(respuesta.status.isSuccess()) { "Error al cargar los lotes: ${respuesta.status.value}" }
        respuesta.body<List<Lote>>()
    }
}