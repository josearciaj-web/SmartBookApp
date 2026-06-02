package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.UsuarioRepository
import co.edu.cecar.smartbookapp.Models.Usuario.Usuario
import kotlinx.coroutines.launch

class UsuarioViewModel : ViewModel() {
    private val repository = UsuarioRepository()

    var listaUsuarios by mutableStateOf<List<Usuario>>(emptyList())
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    init {
        cargarUsuarios()
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.obtenerTodos()
            resultado.onSuccess { usuarios ->
                listaUsuarios = usuarios
                estaCargando = false
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al cargar usuarios"
                estaCargando = false
            }
        }
    }

    fun guardarUsuario(usuario: Usuario, onSuccess: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = if (usuario.id == null || usuario.id == 0) {
                repository.crear(usuario)
            } else {
                repository.actualizar(usuario.id, usuario).map { Unit }
            }

            resultado.onSuccess {
                estaCargando = false
                cargarUsuarios()
                onSuccess()
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al guardar usuario"
                estaCargando = false
            }
        }
    }

    fun eliminarUsuario(id: Int) {
        viewModelScope.launch {
            estaCargando = true
            val resultado = repository.eliminar(id)
            resultado.onSuccess {
                estaCargando = false
                cargarUsuarios()
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al eliminar usuario"
                estaCargando = false
            }
        }
    }
}
