package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.ViewModel.LoteViewModel
import kotlin.collections.filter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLotes(
    volverDashboard: () -> Unit,
    viewModel: LoteViewModel = viewModel()
) {
    var buscarTexto by remember { mutableStateOf("") }
    var mostrarDialogoCrear by remember { mutableStateOf(false) }
    var nuevoLoteInput by remember { mutableStateOf("") }

    val listaLotes = viewModel.listaLotes
    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError


    LaunchedEffect(Unit) {
        viewModel.cargarLotes()
    }

    if (mostrarDialogoCrear) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoCrear = false },
            title = { Text("Registrar Nuevo Lote", fontWeight = FontWeight.Bold, color = Color(0xFF0B3A5B)) },
            text = {
                Column {
                    Text("Ingrese el código numérico para el nuevo lote:", fontSize = 14.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = nuevoLoteInput,
                        onValueChange = { nuevoLoteInput = it },
                        placeholder = { Text("Ej: 20291") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val loteInt = nuevoLoteInput.toIntOrNull()
                        if (loteInt != null) {
                            viewModel.registrarLote(loteInt) {
                                nuevoLoteInput = ""
                                mostrarDialogoCrear = false
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                    enabled = nuevoLoteInput.isNotBlank() && !estaCargando
                ) {
                    Text("Guardar Lote")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogoCrear = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = { volverDashboard() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver al Dashboard")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Gestión de Lotes", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Administra los lotes de libros del sistema SmartBook", color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { mostrarDialogoCrear = true },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("+ Nuevo Lote", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = buscarTexto,
            onValueChange = { buscarTexto = it },
            placeholder = { Text("Buscar por código de lote...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFFD32F2F))
        )

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Layers, null, tint = Color(0xFF0B3A5B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Listado de Lotes", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B3A5B))
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Lote", fontWeight = FontWeight.Bold, color = Color.Gray, modifier = Modifier.weight(1f))
                    Text("Actual", fontWeight = FontWeight.Bold, color = Color.Gray, textAlign = TextAlign.End, modifier = Modifier.weight(1f))
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = Color.LightGray)

                if (estaCargando) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFC0392B))
                    }
                } else if (mensajeError.isNotEmpty()) {
                    Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    // Filtrado en tiempo real
                    val lotesFiltrados = listaLotes.filter {
                        it.codigo.toString().contains(buscarTexto)
                    }

                    lotesFiltrados.forEach { lote ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = lote.codigo.toString(), fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E2229), modifier = Modifier.weight(1f))

                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.CenterEnd) {
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (lote.actual) Color(0xFFE8F8F5) else Color(0xFFFADBD8)
                                    ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = if (lote.actual) "Sí" else "No",
                                        color = if (lote.actual) Color(0xFF27AE60) else Color(0xFFC0392B),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        HorizontalDivider(color = Color(0xFFF2F4F4))
                    }
                }
            }
        }
    }
}