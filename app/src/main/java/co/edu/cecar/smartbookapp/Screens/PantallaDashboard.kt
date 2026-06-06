package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Token.SessionManager
import co.edu.cecar.smartbookapp.ViewModel.DashboardViewModel
import java.util.Locale

@Composable
fun PantallaDashboard(
    viewModel: DashboardViewModel = viewModel(),
    cerrarSesion: () -> Unit,
    irALotes: () -> Unit,
    irAClientes: () -> Unit,
    irALibros: () -> Unit,
    irAInventario: () -> Unit,
    irAVentas: () -> Unit,
    irAUsuarios: () -> Unit
) {
    val dashboardData = viewModel.dashboardData
    val estaCargando = viewModel.estaCargando
    val nombreUsuario = SessionManager.nombreUsuario ?: "Administrador CDI"
    val correoUsuario = SessionManager.correoUsuario ?: "admin@cecar.edu.co"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CDI CECAR",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color(0xFFD32F2F)
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.End) {
                Text(nombreUsuario, fontSize = 14.sp, color = Color(0xFF1E2229))
                Text(correoUsuario, fontSize = 11.sp, color = Color.Gray)
            }
        }

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = "Dashboard",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E2229)
            )

            Text(
                text = "Bienvenido al sistema SmartBook",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (estaCargando) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFD32F2F))
                }
            } else if (viewModel.mensajeError.isNotEmpty()) {
                Text(text = viewModel.mensajeError, color = Color.Red, modifier = Modifier.padding(16.dp))
                Button(onClick = { viewModel.cargarDashboard() }) {
                    Text("Reintentar")
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth()) {
                    TarjetaIndicador("Total Clientes", "${dashboardData?.totalClientes ?: 0}", Icons.Default.Person, Color(0xFFD32F2F), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    TarjetaIndicador("Libros Stock", "${dashboardData?.totalLibros ?: 0}", Icons.Default.MenuBook, Color(0xFF1E2229), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    TarjetaIndicador("Ventas Mes", "0", Icons.Default.ShoppingBag, Color(0xFFD32F2F), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    TarjetaIndicador("Ver Lotes", "Activos", Icons.Default.Inventory, Color(0xFF1E2229), Modifier.weight(1f).clickable { irALotes() })
                }

                Spacer(modifier = Modifier.height(12.dp))

                TarjetaIndicador("Ver Inventario General", "Stock", Icons.Default.MenuBook, Color(0xFF3F3F98), Modifier.fillMaxWidth().clickable { irAInventario() })

                Spacer(modifier = Modifier.height(12.dp))

                TarjetaIndicador("Gestionar Usuarios", "Accesos", Icons.Default.Person, Color(0xFF27AE60), Modifier.fillMaxWidth().clickable { irAUsuarios() })
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { cerrarSesion() },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(Icons.Default.Logout, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
fun TarjetaIndicador(
    titulo: String,
    valor: String,
    icono: ImageVector,
    colorSuperior: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(130.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            HorizontalDivider(color = colorSuperior, thickness = 4.dp, modifier = Modifier.width(40.dp))

            Spacer(modifier = Modifier.height(12.dp))

            Text(titulo, fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(valor, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2229))
            
            Spacer(modifier = Modifier.weight(1f))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
                Icon(icono, null, tint = colorSuperior.copy(alpha = 0.2f), modifier = Modifier.size(40.dp))
            }
        }
    }
}
