package co.edu.cecar.smartbookapp.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.ingresosRepository
import co.edu.cecar.smartbookapp.Models.Ingresos.Ingreso
import kotlinx.coroutines.launch

class IngresoViewModel : ViewModel() {
    private val repository = ingresosRepository()

    var listaIngresos by mutableStateOf<List<Ingreso>>(emptyList())
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    val totalIngresosCount: Int get() = listaIngresos.size
    val unidadesTotalesSum: Int get() = listaIngresos.sumOf { it.unidades }

    fun cargarIngresos() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.obtenerIngresos()
            resultado.onSuccess {
                listaIngresos = it
                estaCargando = false
            }.onFailure {
                mensajeError = it.message ?: "Fallo al conectar con el servidor"
                estaCargando = false
            }
        }
    }
}