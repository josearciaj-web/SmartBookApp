package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.LibroRepository
import co.edu.cecar.smartbookapp.Models.Libros.Libro
import co.edu.cecar.smartbookapp.Models.Libros.CrearLibro
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

            val resultado = repository.obtenerLibros()

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
            mensajeError = ""

            val resultado = if (libro.id == null || libro.id == 0) {
                val libroCrearDto = CrearLibro(
                    nombre = libro.nombre,
                    nivel = libro.nivel,
                    tipo = libro.tipo,
                    edicion = libro.edicion,
                    unidades = libro.unidades,
                    lote = libro.lote,
                    valorCompra = libro.valorCompra,
                    valorVentaPublico = libro.valorVentaPublico
                )
                // Modificado: Ya recibe el Result<Unit> directo del repositorio limpio
                repository.crearLibro(libroCrearDto)
            } else {
                repository.actualizarLibro(libro.id, libro)
            }

            resultado.onSuccess {
                estaCargando = false
                cargarLibros() // Recarga la lista de libros actualizados
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
            mensajeError = ""

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