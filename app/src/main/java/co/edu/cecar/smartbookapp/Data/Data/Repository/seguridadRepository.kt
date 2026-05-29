package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.Seguridad.AuthResponse
import co.edu.cecar.smartbookapp.Models.Seguridad.LoginRequest
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class seguridadRepository {
    private val client = HttpClientProvider.client
    // Ruta extraída del Swagger: /api/Seguridad/iniciar-sesion
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Seguridad/iniciar-sesion"

    suspend fun iniciarSesion(credenciales: LoginRequest): Result<AuthResponse> = runCatching {
        val respuesta = client.post(BASE_URL_API) {
            setBody(credenciales)
        }
        
        if (!respuesta.status.isSuccess()) {
            val errorBody = try { respuesta.body<String>() } catch (e: Exception) { "" }
            throw Exception("Error ${respuesta.status.value}: ${respuesta.status.description}. $errorBody")
        }

        respuesta.body<AuthResponse>()
    }
}
