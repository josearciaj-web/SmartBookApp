package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Libros.CrearLibro
import co.edu.cecar.smartbookapp.Models.Libros.Libro
import co.edu.cecar.smartbookapp.Models.RespuestaError.RespuestaError
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class LibroRepository {
    private val client = HttpClientProvider.client
    private val jsonDecoder = Json { ignoreUnknownKeys = true }
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Libros"

    suspend fun obtenerLibros(): Result<List<Libro>> = runCatching {
        val respuesta = client.get(BASE_URL_API)
        check(respuesta.status.isSuccess()) {
            "Error al cargar libros: ${respuesta.status.value}"
        }
        respuesta.body<List<Libro>>()
    }

    suspend fun libroPorId(idQuery: Int): Result<Libro> = runCatching {
        val respuesta = client.get("$BASE_URL_API/$idQuery")
        check(respuesta.status.isSuccess()) {
            "Error al cargar el libro: ${respuesta.status.value}"
        }
        respuesta.body<Libro>()
    }

    suspend fun crearLibro(libro: CrearLibro): Result<Unit> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(libro)
        }

        if (!respuesta.status.isSuccess()) {
            val jsonCrudo = respuesta.bodyAsText()

            val mensajeServidor = try {
                val errorParseado = jsonDecoder.decodeFromString<RespuestaError>(jsonCrudo)
                errorParseado.detail ?: "No se pudo registrar el libro. Verifique los datos."
            } catch (e: Exception) {
                "Error inesperado en el servidor. Intente más tarde."
            }

            val mensajeCamuflado = when {
                mensajeServidor.contains("ya existe en el lote", ignoreCase = true) -> {
                    "No se puede registrar: Este libro ya se encuentra creado en el lote seleccionado."
                }
                mensajeServidor.contains("ya existe", ignoreCase = true) -> {
                    "Este libro o código ya está registrado en el sistema."
                }
                else -> mensajeServidor
            }

            throw Exception(mensajeCamuflado)
        }
    }
    suspend fun actualizarLibro(idQuery: Int, libroModificado: Libro): Result<Unit> = runCatching {
        val respuesta = client.put("$BASE_URL_API/$idQuery") {
            setBody(libroModificado)
        }
        check(respuesta.status.isSuccess()) {
            "Error al actualizar libro: ${respuesta.status.value}"
        }
    }

    suspend fun eliminarLibro(idQuery: Int): Result<Unit> = runCatching {
        val respuesta = client.delete("$BASE_URL_API/$idQuery")
        check(respuesta.status.isSuccess()) {
            "Error al eliminar libro: ${respuesta.status.value}"
        }
    }
}