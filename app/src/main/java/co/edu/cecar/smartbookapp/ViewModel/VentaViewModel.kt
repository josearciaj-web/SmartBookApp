package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.VentaRepository
import co.edu.cecar.smartbookapp.Models.Venta.Venta
import kotlinx.coroutines.launch

class VentaViewModel : ViewModel() {
    private val repository = VentaRepository()

    var listaVentas by mutableStateOf<List<Venta>>(emptyList())
        private set

    var ventaSeleccionada by mutableStateOf<Venta?>(null)
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

    fun realizarVenta(venta: Venta, onSuccess: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""
            val resultado = repository.realizarVenta(venta)
            resultado.onSuccess {
                estaCargando = false
                cargarVentas()
                onSuccess()
            }.onFailure { error ->
                mensajeError = error.message ?: "Error al procesar la venta"
                estaCargando = false
            }
        }
    }
}
