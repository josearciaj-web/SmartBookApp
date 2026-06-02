package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.R
import co.edu.cecar.smartbookapp.ViewModel.LibroViewModel

@Composable
fun PantallaLibros(
    volverDashboard: () -> Unit,
    irNuevoLibro: () -> Unit,
    irEditarLibro: (Int) -> Unit,
    viewModel: LibroViewModel = viewModel()
) {
    var buscarState by remember { mutableStateOf("") }
    val libros = viewModel.listaLibros.filter {
        it.nombre.contains(buscarState, ignoreCase = true) ||
        it.nivel.contains(buscarState, ignoreCase = true)
    }
    val estaCargando = viewModel.estaCargando

    // REFRESCO AUTOMÁTICO AL ENTRAR
    LaunchedEffect(Unit) {
        viewModel.cargarLibros()
    }

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

        Text("Gestión de Libros", fontSize = 30.sp, color = Color(0xFF1A3A5C))
        Text("Administra los libros del sistema SmartBook", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { irNuevoLibro() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nuevo Libro")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = buscarState,
            onValueChange = { buscarState = it },
            placeholder = { Text("Buscar por nombre, nivel o tipo...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

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
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color(0xFF3F3F98))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Listado de Libros", color = Color(0xFF1A3A5C), fontSize = 20.sp)
                }

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Column {
                        FilaEncabezadoLibros()
                        HorizontalDivider()
                        
                        if (estaCargando) {
                            Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else if (libros.isEmpty()) {
                            Text("No se encontraron libros", modifier = Modifier.padding(20.dp))
                        } else {
                            libros.forEach { libro ->
                                FilaLibro(
                                    libro.id ?: 0,
                                    libro.nombre,
                                    libro.nivel,
                                    if (libro.tipo == 1) "Workbook" else "StudentBook",
                                    libro.edicion,
                                    libro.lote.toString(),
                                    libro.unidades.toString(),
                                    irEditarLibro,
                                    onEliminar = { viewModel.eliminarLibro(libro.id ?: 0) }
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
fun FilaEncabezadoLibros() {
    Row(modifier = Modifier.width(930.dp)) {
        Text("Nombre", modifier = Modifier.width(210.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Nivel", modifier = Modifier.width(140.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Tipo", modifier = Modifier.width(150.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Edición", modifier = Modifier.width(100.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Lote", modifier = Modifier.width(100.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Stock", modifier = Modifier.width(90.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Acciones", modifier = Modifier.width(140.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
    }
}

@Composable
fun FilaLibro(
    id: Int,
    nombre: String,
    nivel: String,
    tipo: String,
    edicion: String,
    lote: String,
    stock: String,
    irEditarLibro: (Int) -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(930.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(nombre, modifier = Modifier.width(210.dp), fontSize = 14.sp)
        Text(nivel, modifier = Modifier.width(140.dp), fontSize = 14.sp)
        Text(tipo, modifier = Modifier.width(150.dp), fontSize = 14.sp)
        Text(edicion, modifier = Modifier.width(100.dp), fontSize = 14.sp)
        Text(lote, modifier = Modifier.width(100.dp), fontSize = 14.sp)
        Text(stock, modifier = Modifier.width(90.dp), fontSize = 14.sp)
        Row(modifier = Modifier.width(140.dp)) {
            Text(
                "Editar",
                modifier = Modifier
                    .clickable { irEditarLibro(id) },
                color = Color(0xFFC0392B),
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                "Borrar",
                modifier = Modifier
                    .clickable { onEliminar() },
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}