package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AllInbox
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.ViewModel.IngresoViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaGestionIngresos(
    irAFormularioIngreso: () -> Unit,
    volverDashboard: () -> Unit,
    viewModel: IngresoViewModel = viewModel()
) {
    val formatoMoneda = remember { NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply { maximumFractionDigits = 0 } }

    val listaIngresos = viewModel.listaIngresos
    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError

    LaunchedEffect(Unit) {
        viewModel.cargarIngresos()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // 1. TOP BAR SUPERIOR
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Ingresos", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2229))
                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountCircle, null, tint = Color(0xFF1A3A5C), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Administrador", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E2229))
                }

                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { viewModel.cargarIngresos() }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Refresh, "Refrescar", tint = Color(0xFFC0392B))
                }
            }

            // 2. TÍTULO SECCIÓN
            Text(
                text = "Gestión de Ingresos",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0B3A5B),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // 3. TARJETAS DE INDICADORES EN VIVO
            Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
                TarjetaMiniIndicador(
                    titulo = "Total Registros",
                    valor = viewModel.totalIngresosCount.toString(),
                    icono = Icons.Default.AllInbox,
                    colorIcono = Color(0xFFC0392B),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(12.dp))
                TarjetaMiniIndicador(
                    titulo = "Unidades Totales",
                    valor = viewModel.unidadesTotalesSum.toString(),
                    icono = Icons.Default.Layers,
                    colorIcono = Color(0xFF2980B9),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4. CONTROL DE FLUJOS DE RED Y DETALLES VIVOS
            if (estaCargando) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFC0392B))
                }
            } else if (mensajeError.isNotEmpty()) {
                Text(mensajeError, color = Color.Red, modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center)
            } else {
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    listaIngresos.forEach { ingreso ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    // Mapeo de tu campo '.lote'
                                    Text(text = "Lote #${ingreso.lote}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFFC0392B))
                                    // Usamos el ID del registro como metadato de control
                                    Text(text = "Reg: #${ingreso.id ?: 0}", fontSize = 13.sp, color = Color.LightGray)
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column {
                                        Text("Unidades Ingresadas", fontSize = 12.sp, color = Color.Gray)
                                        // Mapeo de tu campo '.unidades'
                                        Text("${ingreso.unidades} unds", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A5C))
                                    }

                                    Column(horizontalAlignment = Alignment.End) {
                                        Text("Valor Venta Público", fontSize = 12.sp, color = Color.Gray)
                                        // Mapeo de tu campo '.valorVentaPublico'
                                        Text(
                                            text = if(ingreso.valorVentaPublico > 0) formatoMoneda.format(ingreso.valorVentaPublico) else "$0",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF27AE60)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                // Mapeo de tu campo '.valorCompra'
                                Text(text = "Costo adquisición: ${formatoMoneda.format(ingreso.valorCompra)}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = Color.DarkGray)
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
