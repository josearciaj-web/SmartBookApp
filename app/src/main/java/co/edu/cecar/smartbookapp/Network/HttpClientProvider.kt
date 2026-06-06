package co.edu.cecar.smartbookapp

import co.edu.cecar.smartbookapp.Token.SessionManager
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.plugin
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object HttpClientProvider {
    val client = HttpClient(CIO) {
        install(Logging) {
            logger = Logger.ANDROID
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                isLenient = true
                encodeDefaults = false // No enviar campos con valores por defecto (como id=null)
                coerceInputValues = true // Tratar de convertir tipos si es posible
            })
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 20_000
            connectTimeoutMillis = 15_000
            socketTimeoutMillis = 20_000
        }
        defaultRequest {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Accept, ContentType.Application.Json)
        }
    }.apply {
        // INTERCEPTOR DINÁMICO: Agrega el token actualizado en cada petición
        plugin(HttpSend).intercept { request ->
            SessionManager.token?.let {
                request.header(HttpHeaders.Authorization, "Bearer $it")
            }
            execute(request)
        }
    }
}
