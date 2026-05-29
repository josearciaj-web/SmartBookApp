package co.edu.cecar.smartbookapp.Data.Data.Repository

import co.edu.cecar.smartbookapp.HttpClientProvider
import co.edu.cecar.smartbookapp.Models.DashBoard.dashboardResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.isSuccess

class DashboardRepository {
    private val client = HttpClientProvider.client
    private val BASE_URL_API = "https://api.smartbooks.cecar.cloud/api/Dashboard"

    suspend fun obtenerDashboard(): Result<dashboardResponse> = runCatching {
        val respuesta = client.get(BASE_URL_API)
        if (!respuesta.status.isSuccess()) {
            throw Exception("Error al cargar el dashboard: ${respuesta.status.value}")
        }
        respuesta.body<dashboardResponse>()
    }
}
