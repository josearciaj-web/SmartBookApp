package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Models.Venta.DetalleVenta
import co.edu.cecar.smartbookapp.Models.Venta.Venta
import co.edu.cecar.smartbookapp.Token.SessionManager
import co.edu.cecar.smartbookapp.ViewModel.LibroViewModel
import co.edu.cecar.smartbookapp.ViewModel.VentaViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaFormularioVenta(
    volverVentas: () -> Unit,
    ventaViewModel: VentaViewModel = viewModel(),
    libroViewModel: LibroViewModel = viewModel()
) {
    var identificacion by remember { mutableStateOf("") }
    
    // Estado para la selección de libros
    var libroSeleccionado by remember { mutableStateOf<co.edu.cecar.smartbookapp.Models.Libros.Libro?>(null) }
    var cantidad by remember { mutableStateOf("1") }
    
    // Lista de items agregados a la venta actual
    val itemsAgregados = remember { mutableStateListOf<DetalleVenta>() }
    // Nombres para mostrar en la UI
    val nombresLibros = remember { mutableStateMapOf<Int, String>() }

    val estaCargando = ventaViewModel.estaCargando
    val mensajeError = ventaViewModel.mensajeError

    val totalVenta = itemsAgregados.sumOf { it.cantidad * it.precioUnitario }

    // CARGAR LIBROS AUTOMÁTICAMENTE AL ENTRAR
    LaunchedEffect(Unit) {
        libroViewModel.cargarLibros()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = { volverVentas() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Registrar Venta (POS)", color = Color.White, fontSize = 24.sp)
                Spacer(modifier = Modifier.height(6.dp))
                Text("Complete los datos de la venta", color = Color.White, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFC0392B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Datos del Cliente", fontSize = 20.sp, color = Color(0xFF1A3A5C))
                }

                CampoVenta("Identificación del cliente", "Ej: 1052067315", identificacion, { identificacion = it }, KeyboardType.Number)

                if (mensajeError.isNotEmpty()) {
                    Text(mensajeError, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color(0xFF3F3F98))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Items de Venta", fontSize = 20.sp, color = Color(0xFF1A3A5C))
                }

                Text("Agregar Libro", fontSize = 14.sp, color = Color(0xFF1A3A5C), modifier = Modifier.padding(top = 10.dp))
                
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = libroSeleccionado?.nombre ?: "Seleccione un libro",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            IconButton(onClick = { expanded = true }) {
                                Icon(Icons.Default.Add, contentDescription = null)
                            }
                        }
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        libroViewModel.listaLibros.forEach { l ->
                            DropdownMenuItem(
                                text = { Text("${l.nombre} - Lote: ${l.lote} (Stock: ${l.unidades})") },
                                onClick = {
                                    libroSeleccionado = l
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                if (libroSeleccionado != null) {
                    Text("Precio: $${libroSeleccionado?.valorVentaPublico}", fontSize = 14.sp, color = Color.Gray)
                    CampoVenta("Cantidad", "1", cantidad, { cantidad = it }, KeyboardType.Number)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        libroSeleccionado?.let { l ->
                            val cant = cantidad.toIntOrNull() ?: 0
                            if (cant > 0 && cant <= l.unidades) {
                                val item = DetalleVenta(
                                    libroId = l.id ?: 0,
                                    cantidad = cant,
                                    precioUnitario = l.valorVentaPublico
                                )
                                itemsAgregados.add(item)
                                nombresLibros[l.id ?: 0] = l.nombre
                                // Reset selección
                                libroSeleccionado = null
                                cantidad = "1"
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3F98)),
                    enabled = libroSeleccionado != null
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Agregar al Carrito")
                }
            }
        }

        if (itemsAgregados.isNotEmpty()) {
            Spacer(modifier = Modifier.height(18.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Resumen de Items", fontSize = 18.sp, color = Color(0xFF1A3A5C))
                    itemsAgregados.forEachIndexed { index, item ->
                        Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("${nombresLibros[item.libroId]} x${item.cantidad}", modifier = Modifier.weight(1f))
                            Text("$${item.cantidad * item.precioUnitario}")
                            IconButton(onClick = { itemsAgregados.removeAt(index) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red, modifier = Modifier.size(16.dp))
                            }
                        }
                        HorizontalDivider()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Total a pagar:", color = Color.White, fontSize = 18.sp)
                Text("$ $totalVenta", color = Color.White, fontSize = 32.sp)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { volverVentas() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDC3C7)),
                enabled = !estaCargando
            ) {
                Text("Cancelar", color = Color(0xFF1A3A5C))
            }

            Spacer(modifier = Modifier.width(10.dp))

            Button(
                onClick = {
                    if (identificacion.isNotBlank() && itemsAgregados.isNotEmpty()) {
                        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                        val fechaActual = sdf.format(Date())
                        
                        val nuevaVenta = Venta(
                            clienteId = identificacion,
                            usuarioId = SessionManager.usuarioId ?: 1,
                            fecha = fechaActual,
                            total = totalVenta,
                            detalles = itemsAgregados.toList()
                        )
                        ventaViewModel.realizarVenta(nuevaVenta) {
                            volverVentas()
                        }
                    }
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                enabled = !estaCargando && identificacion.isNotBlank() && itemsAgregados.isNotEmpty()
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Registrar Venta")
                }
            }
        }
    }
}

@Composable
fun CampoVenta(
    label: String,
    placeholder: String,
    valor: String,
    onChange: (String) -> Unit,
    keyboardType: KeyboardType
) {
    Text(
        text = label,
        fontSize = 14.sp,
        color = Color(0xFF1A3A5C),
        modifier = Modifier.padding(top = 10.dp)
    )

    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        placeholder = { Text(placeholder) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}