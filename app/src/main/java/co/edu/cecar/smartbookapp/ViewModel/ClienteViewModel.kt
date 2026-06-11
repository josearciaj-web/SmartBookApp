package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.ClienteRepository
import co.edu.cecar.smartbookapp.Models.Clientes.BuscarCliente
import co.edu.cecar.smartbookapp.Models.Clientes.CrearCliente
import kotlinx.coroutines.launch

class ClienteViewModel : ViewModel() {
    private val repository = ClienteRepository()

    var listaClientes by mutableStateOf<List<BuscarCliente>>(emptyList())
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    var operacionExitosa by mutableStateOf(false)
        private set

    init {
        cargarClientes()
    }

    fun cargarClientes() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.buscarPorNombre("")
            resultado.onSuccess {
                listaClientes = it
                estaCargando = false
            }.onFailure {
                mensajeError = it.message ?: "Error al cargar clientes"
                estaCargando = false
            }
        }
    }

    fun registrarCliente(cliente: CrearCliente, onExito: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""

            val fechaCorregida = if (cliente.fechaNacimiento.contains("/")) {
                val partes = cliente.fechaNacimiento.split("/")
                if (partes.size == 3) {
                    "${partes[2]}-${partes[1]}-${partes[0]}"
                } else cliente.fechaNacimiento
            } else cliente.fechaNacimiento

            val clienteParaEnviar = cliente.copy(fechaNacimiento = fechaCorregida)
            val resultado = repository.crearCliente(clienteParaEnviar)

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
