package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.ClienteRepository
import co.edu.cecar.smartbookapp.Models.Clientes.CrearCliente
import kotlinx.coroutines.launch

class ClienteViewModel : ViewModel() {
    private val repository = ClienteRepository()

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    var operacionExitosa by mutableStateOf(false)
        private set

    fun registrarCliente(cliente: CrearCliente, onExito: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.crear(cliente)

            resultado.onSuccess {
                estaCargando = false
                operacionExitosa = true
                onExito()
            }.onFailure {
                estaCargando = false
                mensajeError = it.message ?: "Error desconocido al registrar cliente"
            }
        }
    }

    fun limpiarEstado() {
        mensajeError = ""
        operacionExitosa = false
    }
}
