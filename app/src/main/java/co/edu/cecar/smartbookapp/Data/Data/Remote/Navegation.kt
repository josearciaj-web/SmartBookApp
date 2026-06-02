package co.edu.cecar.smartbookapp.Data.Data.Remote

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
object LoginRoute : NavKey

@Serializable
object PrincipalRoute : NavKey

@Serializable
object OlvideMiContrasenaRoute : NavKey

@Serializable
object RestablecerContrasenaRoute : NavKey

@Serializable
object LotesRoute : NavKey

@Serializable
object ClientesRoute : NavKey

@Serializable
object NuevoClienteRoute : NavKey

@Serializable
object EditarClienteRoute : NavKey

@Serializable
object LibrosRoute : NavKey

@Serializable
object NuevoLibroRoute : NavKey

@Serializable
data class EditarLibroRoute(val id: Int) : NavKey

@Serializable
object InventarioRoute : NavKey

@Serializable
object VentasRoute : NavKey

@Serializable
object NuevaVentaRoute : NavKey

@Serializable
object UsuariosRoute : NavKey

@Serializable
object NuevoUsuarioRoute : NavKey

@Serializable
data class EditarUsuarioRoute(val id: Int) : NavKey

@Serializable
data class DetalleVentaRoute(val id: Int) : NavKey