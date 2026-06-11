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

            val credenciales = LoginRequest(email = usuario, password = contrasena)
            val resultado = repository.iniciarSesion(credenciales)

            resultado.onSuccess { authResponse ->
                estaCargando = false
                SessionManager.guardarSession(
                    token = authResponse.token,
                    nombre = authResponse.nombres,
                    correo = credenciales.email,
                    rol = authResponse.rol,
                    id = authResponse.usuarioId
                )
                onSuccess()
            }.onFailure { excepcion ->
                estaCargando = false
                mensajeError = excepcion.message ?: "Credenciales inválidas"
            }
        }
    }
}
