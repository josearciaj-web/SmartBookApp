package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Ingresos.Ingreso
import co.edu.cecar.smartbookapp.Models.Ingresos.lote
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class ingresosRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Ingresos"

    suspend fun crearIngreso(ingreso: Ingreso): Result<Ingreso> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(ingreso)
        }
        check(respuesta.status.isSuccess()) {
            "Error al registrar ingreso: ${respuesta.status.value}"
        }
        respuesta.body<Ingreso>()
    }

    suspend fun obtenerIngresos(
        desde: String? = null,
        hasta: String? = null,
        lote: Int? = null,
        libroid: Int? = null
    ): Result<List<Ingreso>> = runCatching {
        val respuesta = client.get(BASE_URL_API) {
            if (desde != null) parameter("desde", desde)
            if (hasta != null) parameter("hasta", hasta)
            if (lote != null) parameter("lote", lote)
            if (libroid != null) parameter("libroid", libroid)
        }
        check(respuesta.status.isSuccess()) { "error al obtener ingresos : ${respuesta.status.value}" }
        respuesta.body<List<Ingreso>>()
    }

    suspend fun buscarPorId(idQuery: Int): Result<Ingreso> = runCatching {
        val respuesta = client.get("${BASE_URL_API}/$idQuery")
        check(respuesta.status.isSuccess()) { "Error al buscar ingreso por ID: ${respuesta.status.value}" }
        respuesta.body<Ingreso>()
    }

    suspend fun obtenerLotesDisponibles(): Result<List<lote>> = runCatching {
        val respuesta = client.get("${BASE_URL_API}/lotes")

        check(respuesta.status.isSuccess()) { "Error al traer lotes: ${respuesta.status.value}" }

        respuesta.body<List<lote>>()
    }
}