package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Clientes.BuscarCliente
import co.edu.cecar.smartbookapp.Models.Clientes.CrearCliente
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class ClienteRepository {

    private val client = HttpClientProvider.client
    // URL exacta según Swagger: /api/Clientes
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Clientes"

    suspend fun crear(cliente: CrearCliente): Result<CrearCliente> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(cliente)
        }
        if (!respuesta.status.isSuccess()) {
            throw Exception("Error al crear cliente: ${respuesta.status.value}")
        }
        respuesta.body<CrearCliente>()
    }

    suspend fun buscarPorNombre(nombreQuery: String): Result<List<BuscarCliente>> = runCatching {
        val respuesta = client.get(BASE_URL_API) {
            parameter("nombres", nombreQuery)
        }
        respuesta.body<List<BuscarCliente>>()
    }

    suspend fun buscarPorIdentificacion(identificacionQuery: String): Result<CrearCliente> = runCatching {
        val respuesta = client.get("${BASE_URL_API}/$identificacionQuery")
        if (!respuesta.status.isSuccess()) {
            throw Exception("Cliente no encontrado")
        }
        respuesta.body<CrearCliente>()
    }

    suspend fun actualizar(identificacionQuery: String, clienteModificado: CrearCliente): Result<Unit> = runCatching {
        val respuesta = client.put("${BASE_URL_API}/$identificacionQuery") {
            setBody(clienteModificado)
        }
        check(respuesta.status.isSuccess()) { "Error al actualizar: ${respuesta.status.value}" }
    }
}
