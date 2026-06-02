package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.LoteRepository
import co.edu.cecar.smartbookapp.Models.lote.Lote
import kotlinx.coroutines.launch

class LoteViewModel : ViewModel() {
    private val repository = LoteRepository()

    var listaLotes by mutableStateOf<List<Lote>>(emptyList())
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
            resultado.onSuccess { lotes ->
                listaLotes = lotes
                estaCargando = false
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al cargar los lotes"
                estaCargando = false
            }
        }
    }
}
