package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.background
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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PantallaDashboard(
    cerrarSesion: () -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = true, 
                    onClick = {}, 
                    icon = { Icon(Icons.Default.Dashboard, null) }, 
                    label = { Text("Dashboard") }
                )
                NavigationBarItem(
                    selected = false, 
                    onClick = {}, 
                    icon = { Icon(Icons.Default.People, null) }, 
                    label = { Text("Clientes") }
                )
                NavigationBarItem(
                    selected = false, 
                    onClick = {}, 
                    icon = { Icon(Icons.Default.MenuBook, null) }, 
                    label = { Text("Libros") }
                )
                NavigationBarItem(
                    selected = false, 
                    onClick = {}, 
                    icon = { Icon(Icons.Default.ShoppingBag, null) }, 
                    label = { Text("Ventas") }
                )
            }
        }
    ) { paddingValores ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValores)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Reemplacé la imagen por texto para evitar el error del logo inexistente
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
                    Text("Administrador CDI", fontSize = 14.sp, color = Color(0xFF1E2229))
                    Text("admin@cecar.edu.co", fontSize = 11.sp, color = Color.Gray)
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

                Row(modifier = Modifier.fillMaxWidth()) {
                    TarjetaIndicador("Total Clientes", "16", Icons.Default.Person, Color(0xFFD32F2F), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    TarjetaIndicador("Libros Stock", "150", Icons.Default.MenuBook, Color(0xFF1E2229), Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    TarjetaIndicador("Ventas Mes", "14", Icons.Default.ShoppingBag, Color(0xFFD32F2F), Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(10.dp))
                    TarjetaIndicador("Ingresos Mes", "$ 34.2M", Icons.Default.AttachMoney, Color(0xFF1E2229), Modifier.weight(1f))
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
