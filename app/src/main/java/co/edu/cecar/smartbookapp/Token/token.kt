package co.edu.cecar.smartbookapp.Token

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "SmartBookSession"
    private const val KEY_TOKEN = "token"
    private const val KEY_NOMBRE = "nombre"
    private const val KEY_CORREO = "correo"
    private const val KEY_ROL = "rol"
    private const val KEY_ID = "id"

    private var prefs: SharedPreferences? = null

    var token: String? = null
        private set
    var nombreUsuario: String? = null
        private set
    var correoUsuario: String? = null
        private set
    var rolUsuario: String? = null
        private set
    var usuarioId: Int? = null
        private set

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        token = prefs?.getString(KEY_TOKEN, null)
        nombreUsuario = prefs?.getString(KEY_NOMBRE, null)
        correoUsuario = prefs?.getString(KEY_CORREO, null)
        rolUsuario = prefs?.getString(KEY_ROL, null)
        val id = prefs?.getInt(KEY_ID, -1) ?: -1
        usuarioId = if (id != -1) id else null
    }

    fun guardarSession(token: String, nombre: String? = null, correo: String? = null, rol: String? = null, id: Int? = null) {
        this.token = token
        this.nombreUsuario = nombre
        this.correoUsuario = correo
        this.rolUsuario = rol
        this.usuarioId = id

        prefs?.edit()?.apply {
            putString(KEY_TOKEN, token)
            putString(KEY_NOMBRE, nombre)
            putString(KEY_CORREO, correo)
            putString(KEY_ROL, rol)
            putInt(KEY_ID, id ?: -1)
            apply()
        }
    }

    fun estaLogeado(): Boolean {
        return !token.isNullOrBlank()
    }

    fun cerrarSession() {
        token = null
        nombreUsuario = null
        correoUsuario = null
        rolUsuario = null
        usuarioId = null

        prefs?.edit()?.clear()?.apply()
    }
}
