package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.R
import co.edu.cecar.smartbookapp.ViewModel.InventarioViewModel

@Composable
fun PantallaInventario(
    volverDashboard: () -> Unit,
    viewModel: InventarioViewModel = viewModel()
) {
    var loteBuscar by remember { mutableStateOf("") }
    val inventario = viewModel.listaInventario
    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError

    LaunchedEffect(Unit) {
        viewModel.cargarInventario()
    }

    val stockTotal = inventario.sumOf { it.unidades }
    val bajoStock = inventario.count { it.unidades <= 10 }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { volverDashboard() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color(0xFF1A3A5C))
            }

            Image(
                painter = painterResource(id = R.drawable.logo_cdi),
                contentDescription = "Logo CDI",
                modifier = Modifier
                    .width(145.dp)
                    .height(55.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Gestión de Inventario", fontSize = 30.sp, color = Color(0xFF1A3A5C))
        Text("Control y seguimiento del stock de libros", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                loteBuscar = ""
                viewModel.cargarInventario()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3F98))
        ) {
            Icon(Icons.Default.FilterList, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver Todo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TarjetaInventario("Items", "${inventario.size}", Icons.Default.MenuBook, Color(0xFFC0392B), Modifier.weight(1f))
            Spacer(modifier = Modifier.width(10.dp))
            TarjetaInventario("Bajo Stock", bajoStock.toString(), Icons.Default.Warning, Color(0xFF3F3F98), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        TarjetaInventario("Stock Total", stockTotal.toString(), Icons.Default.Inventory, Color(0xFFC0392B), Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {

                Text("FILTRAR POR LOTE", fontSize = 14.sp, color = Color(0xFF1A3A5C))

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = loteBuscar,
                        onValueChange = { loteBuscar = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            val loteInt = loteBuscar.toIntOrNull()
                            viewModel.cargarInventario(loteInt)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3F98))
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Buscar")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Inventory, contentDescription = null, tint = Color(0xFF3F3F98))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Inventario de Libros", color = Color(0xFF1A3A5C), fontSize = 20.sp)
                }

                if (estaCargando) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (mensajeError.isNotEmpty()) {
                    Text(mensajeError, color = Color.Red, modifier = Modifier.padding(16.dp))
                }

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Column {
                        FilaEncabezadoInventario()
                        HorizontalDivider()

                        inventario.forEach { item ->
                            FilaInventario(item)
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaInventario(
    titulo: String,
    valor: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorSuperior: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            HorizontalDivider(color = colorSuperior, thickness = 4.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(titulo, fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(valor, fontSize = 24.sp, color = Color(0xFF1A3A5C))
                }

                Icon(icono, contentDescription = null, tint = colorSuperior, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun FilaEncabezadoInventario() {
    Row(modifier = Modifier.width(900.dp)) {
        Text("Libro", modifier = Modifier.width(260.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Lote", modifier = Modifier.width(130.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Disponibles", modifier = Modifier.width(170.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Ingresados", modifier = Modifier.width(170.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Vendidos", modifier = Modifier.width(170.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
    }
}

@Composable
fun FilaInventario(item: co.edu.cecar.smartbookapp.Models.Inventario.Inventario) {
    Row(
        modifier = Modifier
            .width(900.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val displayNombre = if (item.nombreLibro != "Desconocido") item.nombreLibro else "ID: ${item.libroId}"
        Text(displayNombre, modifier = Modifier.width(260.dp), fontSize = 14.sp)
        Text("${item.lote}", modifier = Modifier.width(130.dp), fontSize = 14.sp)
        Text("${item.unidades}", modifier = Modifier.width(170.dp), fontSize = 14.sp, color = if (item.unidades <= 10) Color.Red else Color.Black)
        Text("${item.cantidadIngresada}", modifier = Modifier.width(170.dp), fontSize = 14.sp)
        Text("${item.cantidadVendida}", modifier = Modifier.width(170.dp), fontSize = 14.sp)
    }
}