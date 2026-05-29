package co.edu.cecar.smartbookapp.ViewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.edu.cecar.smartbookapp.Data.Data.Repository.seguridadRepository
import co.edu.cecar.smartbookapp.Models.Seguridad.LoginRequest
import co.edu.cecar.smartbookapp.Token.SessionManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val repository = seguridadRepository()

    var estaCargando by mutableStateOf(false)
        private set

    var mensajeError by mutableStateOf("")
        private set

    fun autenticarUsuario(usuario: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            estaCargando = true
            mensajeError = ""

            // Los campos en LoginRequest ahora son 'email' y 'password' según el Swagger
            val credenciales = LoginRequest(email = usuario, password = contrasena)
            val resultado = repository.iniciarSesion(credenciales)

            resultado.onSuccess { authResponse ->
                estaCargando = false
                // Guardar token
                SessionManager.guardarSession(authResponse.token)
                // Ejecutar navegación
                onSuccess()
            }.onFailure { excepcion ->
                estaCargando = false
                mensajeError = excepcion.message ?: "Credenciales inválidas"
            }
        }
    }
}
