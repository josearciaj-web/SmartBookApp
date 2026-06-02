package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import co.edu.cecar.smartbookapp.R

import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.ViewModel.VentaViewModel

@Composable
fun PantallaVentas(
    volverDashboard: () -> Unit,
    irNuevaVenta: () -> Unit,
    irDetalleVenta: (Int) -> Unit,
    viewModel: VentaViewModel = viewModel()
) {
    var desde by remember { mutableStateOf("") }
    var hasta by remember { mutableStateOf("") }
    var libro by remember { mutableStateOf("Todos los libros") }

    val listaVentas = viewModel.listaVentas
    val estaCargando = viewModel.estaCargando

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
                modifier = Modifier.width(145.dp).height(55.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Terminal de Ventas (POS)", fontSize = 28.sp, color = Color(0xFF1A3A5C))
        Text("Sistema punto de venta - SmartBook", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { irNuevaVenta() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nueva Venta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text("Filtros de búsqueda", fontSize = 18.sp, color = Color(0xFF1A3A5C))

                Spacer(modifier = Modifier.height(10.dp))

                CampoFiltroVenta("Desde", "dd/mm/aaaa", desde) { desde = it }
                CampoFiltroVenta("Hasta", "dd/mm/aaaa", hasta) { hasta = it }

                Text("Libro", fontSize = 14.sp, color = Color(0xFF1A3A5C), modifier = Modifier.padding(top = 10.dp))

                OutlinedTextField(
                    value = libro,
                    onValueChange = { libro = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) }
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row {
                    Button(
                        onClick = {},
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3F98))
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Buscar")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = {
                            desde = ""
                            hasta = ""
                            libro = "Todos los libros"
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Limpiar")
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
                Text(
                    text = "Historial de Ventas",
                    fontSize = 20.sp,
                    color = Color(0xFF1A3A5C),
                    modifier = Modifier.padding(14.dp)
                )

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Column {
                        FilaEncabezadoVentas()
                        HorizontalDivider()

                        if (estaCargando) {
                            Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFFC0392B))
                            }
                        } else if (viewModel.mensajeError.isNotEmpty()) {
                            Text(viewModel.mensajeError, color = Color.Red, modifier = Modifier.padding(10.dp))
                        } else if (listaVentas.isEmpty()) {
                            Text("No hay ventas registradas.", modifier = Modifier.padding(10.dp))
                        } else {
                            listaVentas.forEach { venta ->
                                FilaVenta(
                                    recibo = "FAC-${venta.id ?: "000"}",
                                    cliente = venta.clienteId,
                                    total = "$ ${venta.total}",
                                    fecha = venta.fecha,
                                    irDetalleVenta = { venta.id?.let { irDetalleVenta(it) } }
                                )
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CampoFiltroVenta(
    label: String,
    placeholder: String,
    valor: String,
    onChange: (String) -> Unit
) {
    Text(label, fontSize = 14.sp, color = Color(0xFF1A3A5C), modifier = Modifier.padding(top = 10.dp))

    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        trailingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
    )
}

@Composable
fun FilaEncabezadoVentas() {
    Row(modifier = Modifier.width(850.dp)) {
        Text("Recibo", modifier = Modifier.width(190.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Cliente", modifier = Modifier.width(280.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Total", modifier = Modifier.width(130.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Fecha", modifier = Modifier.width(170.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Acciones", modifier = Modifier.width(80.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
    }
}

@Composable
fun FilaVenta(
    recibo: String,
    cliente: String,
    total: String,
    fecha: String,
    irDetalleVenta: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(850.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(recibo, modifier = Modifier.width(190.dp), fontSize = 14.sp)
        Text(cliente, modifier = Modifier.width(280.dp), fontSize = 14.sp)
        Text(total, modifier = Modifier.width(130.dp), fontSize = 14.sp, color = Color(0xFFC0392B))
        Text(fecha, modifier = Modifier.width(170.dp), fontSize = 14.sp)
        Text(
            "Ver",
            modifier = Modifier.width(80.dp).clickable { irDetalleVenta() },
            color = Color(0xFFC0392B),
            fontSize = 14.sp
        )
    }
}