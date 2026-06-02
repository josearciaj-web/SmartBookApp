package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.ViewModel.VentaViewModel

@Composable
fun PantallaDetalleVenta(
    ventaId: Int,
    volverVentas: () -> Unit,
    viewModel: VentaViewModel = viewModel()
) {
    val venta = viewModel.ventaSeleccionada
    val estaCargando = viewModel.estaCargando

    LaunchedEffect(ventaId) {
        viewModel.buscarVentaPorId(ventaId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(onClick = { volverVentas() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        if (estaCargando) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFC0392B))
            }
        } else if (venta != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Column {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B))
                    ) {
                        Row(modifier = Modifier.padding(20.dp)) {
                            Icon(Icons.Default.Receipt, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Detalle de Venta", color = Color.White, fontSize = 24.sp)
                        }
                    }

                    Column(modifier = Modifier.padding(20.dp)) {
                        Text("Número de Recibo:", fontSize = 14.sp, color = Color.Gray)
                        Text("FAC-${venta.id}", fontSize = 18.sp, color = Color(0xFF1A3A5C))

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Cliente:", fontSize = 14.sp, color = Color.Gray)
                        Text(venta.clienteId, fontSize = 18.sp, color = Color(0xFF1A3A5C))

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Total:", fontSize = 14.sp, color = Color.Gray)
                        Text("$${venta.total}", fontSize = 24.sp, color = Color(0xFFC0392B))

                        Spacer(modifier = Modifier.height(14.dp))

                        Text("Fecha:", fontSize = 14.sp, color = Color.Gray)
                        Text(venta.fecha, fontSize = 18.sp, color = Color(0xFF1A3A5C))

                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Detalles de la compra:",
                            fontSize = 16.sp,
                            color = Color(0xFF1A3A5C),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        venta.detalles.forEach { detalle ->
                            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                Text("Libro ID: ${detalle.libroId}", modifier = Modifier.weight(1f), fontSize = 14.sp)
                                Text("Cant: ${detalle.cantidad}", modifier = Modifier.width(60.dp), fontSize = 14.sp)
                                Text("$${detalle.precioUnitario}", modifier = Modifier.width(80.dp), fontSize = 14.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "La factura en PDF se envía automáticamente al correo del cliente registrado.",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = { volverVentas() },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
                        ) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(viewModel.mensajeError.ifEmpty { "No se pudo cargar el detalle de la venta" })
            }
        }
    }
}