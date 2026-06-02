package co.edu.cecar.smartbookapp.Data.Data.Remote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import co.edu.cecar.smartbookapp.Screens.PantallaClientes
import co.edu.cecar.smartbookapp.Screens.PantallaDashboard
import co.edu.cecar.smartbookapp.Screens.PantallaDetalleVenta
import co.edu.cecar.smartbookapp.Screens.PantallaFormularioCliente
import co.edu.cecar.smartbookapp.Screens.PantallaFormularioLibro
import co.edu.cecar.smartbookapp.Screens.PantallaFormularioVenta
import co.edu.cecar.smartbookapp.Screens.PantallaInventario
import co.edu.cecar.smartbookapp.Screens.PantallaLibros
import co.edu.cecar.smartbookapp.Screens.PantallaLogin
import co.edu.cecar.smartbookapp.Screens.PantallaLotes
import co.edu.cecar.smartbookapp.Screens.PantallaOlvideMiContrasena
import co.edu.cecar.smartbookapp.Screens.PantallaRestablecerContrasena
import co.edu.cecar.smartbookapp.Screens.PantallaFormularioUsuario
import co.edu.cecar.smartbookapp.Screens.PantallaUsuarios
import co.edu.cecar.smartbookapp.Screens.PantallaVentas

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    val backStack = rememberNavBackStack(LoginRoute)

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {

            entry<LoginRoute> {
                PantallaLogin(
                    navegarAPantallaInicio = { backStack.add(PrincipalRoute) },
                    navegarAPatallaRestablecerContrasena = {
                        backStack.add(OlvideMiContrasenaRoute)
                    }
                )
            }

            entry<PrincipalRoute> {
                PantallaDashboard(
                    cerrarSesion = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    },
                    irALotes = {
                        backStack.add(LotesRoute)
                    },
                    irAClientes = {
                        backStack.add(ClientesRoute)
                    },
                    irALibros = {
                        backStack.add(LibrosRoute)
                    },
                    irAInventario = {
                        backStack.add(InventarioRoute)
                    },
                    irAVentas = {
                        backStack.add(VentasRoute)
                    },
                    irAUsuarios = {
                        backStack.add(UsuariosRoute)
                    }
                )
            }

            entry<LotesRoute> {
                PantallaLotes(
                    volverDashboard = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<ClientesRoute> {
                PantallaClientes(
                    volverDashboard = {
                        backStack.removeLastOrNull()
                    },
                    irNuevoCliente = {
                        backStack.add(NuevoClienteRoute)
                    },
                    irEditarCliente = {
                        backStack.add(EditarClienteRoute)
                    }
                )
            }

            entry<NuevoClienteRoute> {
                PantallaFormularioCliente(
                    titulo = "Registrar Cliente",
                    volverClientes = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<EditarClienteRoute> {
                PantallaFormularioCliente(
                    titulo = "Editar Cliente",
                    volverClientes = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<LibrosRoute> {
                PantallaLibros(
                    volverDashboard = {
                        backStack.removeLastOrNull()
                    },
                    irNuevoLibro = {
                        backStack.add(NuevoLibroRoute)
                    },
                    irEditarLibro = { libroId ->
                        backStack.add(EditarLibroRoute(libroId))
                    }
                )
            }

            entry<NuevoLibroRoute> {
                PantallaFormularioLibro(
                    titulo = "Registrar Libro",
                    volverLibros = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<EditarLibroRoute> { route ->
                PantallaFormularioLibro(
                    idLibro = route.id,
                    titulo = "Editar Libro",
                    volverLibros = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<InventarioRoute> {
                PantallaInventario(
                    volverDashboard = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<VentasRoute> {
                PantallaVentas(
                    volverDashboard = {
                        backStack.removeLastOrNull()
                    },
                    irNuevaVenta = {
                        backStack.add(NuevaVentaRoute)
                    },
                    irDetalleVenta = { ventaId ->
                        backStack.add(DetalleVentaRoute(ventaId))
                    }
                )
            }

            entry<NuevaVentaRoute> {
                PantallaFormularioVenta(
                    volverVentas = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<DetalleVentaRoute> { route ->
                PantallaDetalleVenta(
                    ventaId = route.id,
                    volverVentas = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<UsuariosRoute> {
                PantallaUsuarios(
                    volverDashboard = {
                        backStack.removeLastOrNull()
                    },
                    irNuevoUsuario = {
                        backStack.add(NuevoUsuarioRoute)
                    },
                    irEditarUsuario = { usuarioId ->
                        backStack.add(EditarUsuarioRoute(usuarioId))
                    }
                )
            }

            entry<NuevoUsuarioRoute> {
                PantallaFormularioUsuario(
                    titulo = "Registrar Usuario",
                    volverUsuarios = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<EditarUsuarioRoute> { route ->
                PantallaFormularioUsuario(
                    idUsuario = route.id,
                    titulo = "Editar Usuario",
                    volverUsuarios = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            entry<OlvideMiContrasenaRoute> {
                PantallaOlvideMiContrasena(
                    NavegarPantallaLogin = {
                        backStack.removeLastOrNull()
                    },
                    NavegarPantallaRestablecer = {
                        backStack.add(RestablecerContrasenaRoute)
                    }
                )
            }

            entry<RestablecerContrasenaRoute> {
                PantallaRestablecerContrasena(
                    NavegarPantallaLogin = {
                        backStack.clear()
                        backStack.add(LoginRoute)
                    }
                )
            }
        }
    )
}