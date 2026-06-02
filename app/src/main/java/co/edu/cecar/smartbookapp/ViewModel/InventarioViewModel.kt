package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.InventarioRepository
import co.edu.cecar.smartbookapp.Models.Inventario.Inventario
import kotlinx.coroutines.launch

class InventarioViewModel : ViewModel() {
    private val repository = InventarioRepository()

    var listaInventario by mutableStateOf<List<Inventario>>(emptyList())
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    init {
        cargarInventario()
    }

    fun cargarInventario(lote: Int? = null) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.obtenerInventario(lote)
            resultado.onSuccess { data ->
                listaInventario = data
                estaCargando = false
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al cargar el inventario"
                estaCargando = false
            }
        }
    }
}
