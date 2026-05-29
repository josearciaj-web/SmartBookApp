package co.edu.cecar.smartbookapp.Data.Data.Remote

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import co.edu.cecar.smartbookapp.Screens.PantallaDashboard
import co.edu.cecar.smartbookapp.Screens.PantallaLogin
import co.edu.cecar.smartbookapp.Screens.PantallaOlvideMiContrasena
import co.edu.cecar.smartbookapp.Screens.PantallaRestablecerContrasena

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
                    })
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