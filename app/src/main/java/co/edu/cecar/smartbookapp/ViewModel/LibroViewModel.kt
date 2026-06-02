package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.LibroRepository
import co.edu.cecar.smartbookapp.Models.Libros.Libro
import kotlinx.coroutines.launch

class LibroViewModel : ViewModel() {
    private val repository = LibroRepository()

    var listaLibros by mutableStateOf<List<Libro>>(emptyList())
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    init {
        cargarLibros()
    }

    fun cargarLibros() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.obtenerlibros()
            resultado.onSuccess { libros ->
                listaLibros = libros
                estaCargando = false
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al cargar libros"
                estaCargando = false
            }
        }
    }

    fun guardarLibro(libro: Libro, onSuccess: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            val resultado = if (libro.id == null || libro.id == 0) {
                repository.crearLibro(libro)
            } else {
                repository.actualizarLibro(libro.id, libro).map { Unit }
            }

            resultado.onSuccess {
                estaCargando = false
                cargarLibros()
                onSuccess()
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al guardar libro"
                estaCargando = false
            }
        }
    }

    fun eliminarLibro(id: Int) {
        viewModelScope.launch {
            estaCargando = true
            val resultado = repository.eliminarLibro(id)
            resultado.onSuccess {
                estaCargando = false
                cargarLibros()
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al eliminar libro"
                estaCargando = false
            }
        }
    }
}
