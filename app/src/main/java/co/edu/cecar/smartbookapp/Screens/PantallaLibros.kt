package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.ViewModel.LibroViewModel
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaLibros(
    volverDashboard: () -> Unit,
    irNuevoLibro: () -> Unit,
    irEditarLibro: (Int) -> Unit,
    viewModel: LibroViewModel = viewModel()
) {
    var buscarTexto by remember { mutableStateOf("") }

    val listaLibros = viewModel.listaLibros
    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError

    LaunchedEffect(Unit) {
        viewModel.cargarLibros()
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
                Text("Gestión de Libros", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Inventario de textos académicos del Centro de Idiomas", color = Color.White, fontSize = 13.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { irNuevoLibro() },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("+ Nuevo Libro", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = buscarTexto,
            onValueChange = { buscarTexto = it },
            placeholder = { Text("Buscar libro por nombre o nivel...") },
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
                    Icon(Icons.Default.MenuBook, null, tint = Color(0xFF0B3A5B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Catálogo de Textos", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0B3A5B))
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = Color.LightGray)

                if (estaCargando) {
                    Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFC0392B))
                    }
                } else if (mensajeError.isNotEmpty()) {
                    Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
                } else {
                    val librosFiltrados = listaLibros.filter {
                        it.nombre.contains(buscarTexto, ignoreCase = true) || it.nivel.contains(buscarTexto, ignoreCase = true)
                    }

                    if (librosFiltrados.isEmpty()) {
                        Text(
                            text = "No se encontraron libros que coincidan.",
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }

                    librosFiltrados.forEach { libro ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { irEditarLibro(libro.id ?: 0) }
                                .padding(vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = libro.nombre, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E2229))
                                Text(text = "Nivel: ${libro.nivel} | Lote: ${libro.lote}", fontSize = 13.sp, color = Color.Gray)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Stock", fontSize = 11.sp, color = Color.Gray)
                                Spacer(modifier = Modifier.height(2.dp))
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "${libro.stock}",
                                        color = Color(0xFF1A3A5C),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
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