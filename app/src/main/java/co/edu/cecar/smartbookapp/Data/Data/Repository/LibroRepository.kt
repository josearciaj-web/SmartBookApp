package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Libros.Libro
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class LibroRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Libros"

    suspend fun obtenerlibros(): Result<List<Libro>> = runCatching {
        val respuesta = client.get(BASE_URL_API)
        check(respuesta.status.isSuccess()){"Error al cargar libros: ${respuesta.status.value}"}
        respuesta.body<List<Libro>>()
    }

    suspend fun libroPorId(idQuery: Int): Result<Libro> = runCatching {
        val respuesta = client.get("$BASE_URL_API/$idQuery")
        check(respuesta.status.isSuccess()){"Error al cargar el libro: ${respuesta.status.value}"}
        respuesta.body<Libro>()
    }

    suspend fun crearLibro(libro: Libro): Result<Libro> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(libro)
        }
        check(respuesta.status.isSuccess()){"Error al crear libro: ${respuesta.status.value}"}
        respuesta.body<Libro>()
    }

    suspend fun actualizarLibro(idQuery: Int, libroModificado: Libro): Result<Unit> = runCatching {
        val respuesta = client.put("$BASE_URL_API/$idQuery") {
            setBody(libroModificado)
        }
        check(respuesta.status.isSuccess()) { "Error al actualizar libro: ${respuesta.status.value}" }
    }
}
