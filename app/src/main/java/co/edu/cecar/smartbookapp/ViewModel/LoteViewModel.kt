package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.LoteRepository
import co.edu.cecar.smartbookapp.Models.Ingresos.lote
import co.edu.cecar.smartbookapp.Models.lote.LoteResponse
import co.edu.cecar.smartbookapp.Models.lote.Lote
import kotlinx.coroutines.launch

class LoteViewModel : ViewModel() {
    private val repository = LoteRepository()

    var listaLotes by mutableStateOf<List<LoteResponse>>(emptyList())
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    init {
        cargarLotes()
    }

    fun cargarLotes() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.obtenerLotes()
            resultado.onSuccess {
                listaLotes = it
                estaCargando = false
            }.onFailure {
                mensajeError = it.message ?: "Error al conectar con el servidor"
                estaCargando = false
            }
        }
    }

    fun registrarLote(numeroLote: Int, onExito: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val request = lote(lote = numeroLote)
            val resultado = repository.crearLote(request)

            resultado.onSuccess {
                cargarLotes()
                estaCargando = false
                onExito()
            }.onFailure {
                mensajeError = it.message ?: "Error al registrar el lote"
                estaCargando = false
            }
        }
    }
}