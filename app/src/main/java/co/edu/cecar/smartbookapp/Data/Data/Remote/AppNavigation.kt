package co.edu.cecar.smartbookapp.Data.Data.Remote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import co.edu.cecar.smartbookapp.Screens.PantallaGestionIngresos // IMPORTADO: Tu nueva pantalla de ingresos

import kotlinx.serialization.Serializable // Requerido para las rutas de abajo

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {

    val backStack = rememberNavBackStack(LoginRoute)
    val currentRoute = backStack.lastOrNull()

    val mostrarBarra = currentRoute != null && currentRoute !is LoginRoute &&
            currentRoute !is OlvideMiContrasenaRoute && currentRoute !is RestablecerContrasenaRoute

    Scaffold(
        bottomBar = {
            if (mostrarBarra) {
                NavigationBar(containerColor = Color.White) {
                    NavigationBarItem(
                        selected = currentRoute is PrincipalRoute,
                        onClick = {
                            if (currentRoute !is PrincipalRoute) {
                                backStack.clear()
                                backStack.add(PrincipalRoute)
                            }
                        },
                        icon = { Icon(Icons.Default.Dashboard, null) },
                        label = { Text("Inicio") }
                    )
                    NavigationBarItem(
                        selected = currentRoute is ClientesRoute || currentRoute is NuevoClienteRoute || currentRoute is EditarClienteRoute,
                        onClick = {
                            if (currentRoute !is ClientesRoute) {
                                backStack.clear()
                                backStack.add(PrincipalRoute)
                                backStack.add(ClientesRoute)
                            }
                        },
                        icon = { Icon(Icons.Default.People, null) },
                        label = { Text("Clientes") }
                    )
                    NavigationBarItem(
                        selected = currentRoute is LibrosRoute || currentRoute is NuevoLibroRoute || currentRoute is EditarLibroRoute,
                        onClick = {
                            if (currentRoute !is LibrosRoute) {
                                backStack.clear()
                                backStack.add(PrincipalRoute)
                                backStack.add(LibrosRoute)
                            }
                        },
                        icon = { Icon(Icons.Default.MenuBook, null) },
                        label = { Text("Libros") }
                    )
                    NavigationBarItem(
                        selected = currentRoute is VentasRoute || currentRoute is NuevaVentaRoute || currentRoute is DetalleVentaRoute,
                        onClick = {
                            if (currentRoute !is VentasRoute) {
                                backStack.clear()
                                backStack.add(PrincipalRoute)
                                backStack.add(VentasRoute)
                            }
                        },
                        icon = { Icon(Icons.Default.ShoppingBag, null) },
                        label = { Text("Ventas") }
                    )
                    NavigationBarItem(
                        selected = currentRoute is UsuariosRoute || currentRoute is NuevoUsuarioRoute || currentRoute is EditarUsuarioRoute,
                        onClick = {
                            if (currentRoute !is UsuariosRoute) {
                                backStack.clear()
                                backStack.add(PrincipalRoute)
                                backStack.add(UsuariosRoute)
                            }
                        },
                        icon = { Icon(Icons.Default.Person, null) },
                        label = { Text("Usuarios") }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
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
                                // CORREGIDO: Redirige a IngresosRoute para ver la pantalla real conectada a la API
                                backStack.add(IngresosRoute)
                            },
                            irAVentas = {
                                backStack.add(VentasRoute)
                            },
                            irAUsuarios = {
                                backStack.add(UsuariosRoute)
                            }
                        )
                    }

                    entry<IngresosRoute> {
                        PantallaGestionIngresos(
                            irAFormularioIngreso = {
                                backStack.add(NuevoIngresoRoute)
                            },
                            volverDashboard = {
                                backStack.removeLastOrNull()
                            }
                        )
                    }

                    entry<NuevoIngresoRoute> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Button(onClick = { backStack.removeLastOrNull() }) {
                                Text("Formulario de Ingreso - Volver")
                            }
                        }
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
    }
}

