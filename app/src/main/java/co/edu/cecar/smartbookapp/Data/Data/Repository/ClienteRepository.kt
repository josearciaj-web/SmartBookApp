package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Clientes.BuscarCliente
import co.edu.cecar.smartbookapp.Models.Clientes.CrearCliente
import co.edu.cecar.smartbookapp.Models.RespuestaError.RespuestaError
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class ClienteRepository {

    private val client = HttpClientProvider.client
    private val jsonDecoder = Json { ignoreUnknownKeys = true }
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Clientes"

    suspend fun crearCliente(cliente: CrearCliente): Result<Unit> = runCatching {
        val respuesta = client.post("https://api.smartbooks.cecar.cloud/api/Clientes") {
            setBody(cliente)
        }

        if (!respuesta.status.isSuccess()) {
            val jsonCrudo = respuesta.bodyAsText()

            val mensajeServidor = try {
                val errorParseado = jsonDecoder.decodeFromString<RespuestaError>(jsonCrudo)
                errorParseado.detail ?: "No se pudo registrar el cliente."
            } catch (e: Exception) {
                "Error de conexión con el servidor."
            }

            // Camuflaje para clientes
            val mensajeCamuflado = when {
                mensajeServidor.contains("ya existe", ignoreCase = true) || jsonCrudo.contains("duplicate", ignoreCase = true) -> {
                    "No se puede guardar: Ya existe un cliente registrado con este número de identificación."
                }
                else -> mensajeServidor
            }

            throw Exception(mensajeCamuflado)
        }
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
