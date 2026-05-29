package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.DashboardRepository
import co.edu.cecar.smartbookapp.Models.DashBoard.dashboardResponse
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {
    private val repository = DashboardRepository()

    var dashboardData by mutableStateOf<dashboardResponse?>(null)
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    init {
        cargarDashboard()
    }

    fun cargarDashboard() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.obtenerDashboard()
            resultado.onSuccess { data ->
                dashboardData = data
                estaCargando = false
            }.onFailure { error ->
                mensajeError = error.message ?: "Error desconocido"
                estaCargando = false
            }
        }
    }
}
