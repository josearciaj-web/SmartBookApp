package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Inventario.Inventario
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.isSuccess

class InventarioRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Inventarios"

    suspend fun obtenerInventario(loteQuery: Int? = null): Result<List<Inventario>> = runCatching {
        val respuesta = client.get(BASE_URL_API) {
            if (loteQuery != null) {
                parameter("lote", loteQuery)
            }
        }
        check(respuesta.status.isSuccess()) { "Error al consultar el inventario: ${respuesta.status.value}" }
        respuesta.body<List<Inventario>>()

    }
}


