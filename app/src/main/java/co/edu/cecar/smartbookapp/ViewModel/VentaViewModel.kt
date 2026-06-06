package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.VentaRepository
import co.edu.cecar.smartbookapp.Models.Venta.CrearVentaRequest
import co.edu.cecar.smartbookapp.Models.Venta.VentaResponse
import kotlinx.coroutines.launch

class VentaViewModel : ViewModel() {
    private val repository = VentaRepository()

    // Ahora la lista maneja la respuesta real del servidor (VentaResponse)
    var listaVentas by mutableStateOf<List<VentaResponse>>(emptyList())
        private set

    var ventaSeleccionada by mutableStateOf<VentaResponse?>(null)
        private set

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    init {
        cargarVentas()
    }

    fun cargarVentas() {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.obtenerHistorial()
            resultado.onSuccess { ventas ->
                listaVentas = ventas
                estaCargando = false
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al cargar el historial de ventas"
                estaCargando = false
            }
        }
    }

    fun buscarVentaPorId(id: Int) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.buscarVentaPorId(id)
            resultado.onSuccess { venta ->
                ventaSeleccionada = venta
                estaCargando = false
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al buscar la venta"
                estaCargando = false
            }
        }
    }

    fun realizarVenta(ventaRequest: CrearVentaRequest, onSuccess: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.registrarNuevaVenta(ventaRequest)
            resultado.onSuccess {
                estaCargando = false
                cargarVentas() // Recarga el historial con la nueva venta incluida
                onSuccess()
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al procesar la venta"
                estaCargando = false
            }
        }
    }
}