package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Usuario.Usuario
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.delete
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class UsuarioRepository {

    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Usuarios"

    suspend fun obtenerTodos(): Result<List<Usuario>> = runCatching {
        val respuesta = client.get(BASE_URL_API)
        check(respuesta.status.isSuccess()) { "Error al listar usuarios: ${respuesta.status.value}" }
        respuesta.body<List<Usuario>>()
    }

    suspend fun buscarPorId(idQuery: Int): Result<Usuario> = runCatching {
        val respuesta = client.get("${BASE_URL_API}/$idQuery")
        check(respuesta.status.isSuccess()) { "Error al buscar usuario: ${respuesta.status.value}" }
        respuesta.body<Usuario>()
    }

    suspend fun crear(usuario: Usuario): Result<Usuario> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(usuario)
        }
        check(respuesta.status.isSuccess()) { "Error al crear usuario: ${respuesta.status.value}" }
        respuesta.body<Usuario>()
    }

    suspend fun actualizar(idQuery: Int, usuarioModificado: Usuario): Result<Unit> = runCatching {
        val respuesta = client.put("${BASE_URL_API}/$idQuery") {
            setBody(usuarioModificado)
        }
        check(respuesta.status.isSuccess()) { "Error al actualizar usuario: ${respuesta.status.value}" }
    }

    suspend fun eliminar(idQuery: Int): Result<Unit> = runCatching {
        val respuesta = client.delete("${BASE_URL_API}/$idQuery")
        check(respuesta.status.isSuccess()) { "Error al eliminar usuario: ${respuesta.status.value}" }
    }
}
