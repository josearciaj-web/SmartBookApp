package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Models.Venta.CrearVentaRequest
import co.edu.cecar.smartbookapp.Models.Venta.ItemVentaRequest
import co.edu.cecar.smartbookapp.ViewModel.LibroViewModel
import co.edu.cecar.smartbookapp.ViewModel.VentaViewModel
import java.text.NumberFormat
import java.util.Locale


data class ItemCarrito(
    val libroId: Int,
    val nombre: String,
    val lote: Int,
    val cantidad: Int,
    val precioVenta: Double
) {
    val subtotal: Double get() = precioVenta * cantidad
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioVenta(
    volverVentas: () -> Unit,
    ventaViewModel: VentaViewModel = viewModel(),
    libroViewModel: LibroViewModel = viewModel()
) {

    var identificacion by remember { mutableStateOf("") }
    var numeroComprobante by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }

    var buscarTexto by remember { mutableStateOf("") }
    var libroSeleccionado by remember { mutableStateOf<co.edu.cecar.smartbookapp.Models.Libros.Libro?>(null) }
    var loteSeleccionado by remember { mutableStateOf<Int?>(null) }
    var cantidadInput by remember { mutableStateOf("1") }

    val itemsAgregados = remember { mutableStateListOf<ItemCarrito>() }

    val estaCargando = ventaViewModel.estaCargando
    val mensajeError = ventaViewModel.mensajeError

    val formatoMoneda = remember { NumberFormat.getCurrencyInstance(Locale("es", "CO")).apply { maximumFractionDigits = 0 } }

    val totalVenta = itemsAgregados.sumOf { it.subtotal }

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

        // Encabezado Principal POS
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("Registrar Venta (POS)", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text("Complete los datos de la venta", color = Color.White, fontSize = 14.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFFC0392B))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Datos del Cliente", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A5C))
                }

                Spacer(modifier = Modifier.height(8.dp))

                CampoVenta("Identificación del cliente", "Ej: 1052067315", identificacion, { identificacion = it }, KeyboardType.Number)
                CampoVenta("Número de comprobante", "Ej: 000123", numeroComprobante, { numeroComprobante = it }, KeyboardType.Text)

                Text("Observaciones", fontSize = 14.sp, color = Color(0xFF1A3A5C), modifier = Modifier.padding(top = 10.dp))
                OutlinedTextField(
                    value = observaciones,
                    onValueChange = { observaciones = it },
                    placeholder = { Text("Notas adicionales...") },
                    modifier = Modifier.fillMaxWidth().height(90.dp),
                    maxLines = 3,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null, tint = Color(0xFF3F3F98))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Items de Venta", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A5C))
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    var menuLibrosExpandido by remember { mutableStateOf(false) }

                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = buscarTexto,
                            onValueChange = {
                                buscarTexto = it
                                menuLibrosExpandido = true
                            },
                            placeholder = { Text("Buscar libro...") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                IconButton(onClick = { menuLibrosExpandido = !menuLibrosExpandido }) {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                }
                            }
                        )

                        val librosFiltrados = libroViewModel.listaLibros.filter {
                            it.nombre.contains(buscarTexto, ignoreCase = true) || it.nivel.contains(buscarTexto, ignoreCase = true)
                        }

                        DropdownMenu(
                            expanded = menuLibrosExpandido && librosFiltrados.isNotEmpty(),
                            onDismissRequest = { menuLibrosExpandido = false },
                            modifier = Modifier.fillMaxWidth(0.8f)
                        ) {
                            librosFiltrados.forEach { libro ->
                                DropdownMenuItem(
                                    text = { Text("${libro.nombre} (${libro.nivel})") },
                                    onClick = {
                                        libroSeleccionado = libro
                                        buscarTexto = libro.nombre
                                        loteSeleccionado = libro.lote // Preselecciona el lote base
                                        menuLibrosExpandido = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            libroSeleccionado?.let { libro ->
                                val cant = cantidadInput.toIntOrNull() ?: 1
                                val loteFinal = loteSeleccionado ?: libro.lote

                                val nuevoItem = ItemCarrito(
                                    libroId = libro.id ?: 0,
                                    nombre = libro.nombre,
                                    lote = loteFinal,
                                    cantidad = cant,
                                    precioVenta = libro.valorVentaPublico
                                )
                                itemsAgregados.add(nuevoItem)

                                // Limpieza de selectores
                                libroSeleccionado = null
                                loteSeleccionado = null
                                buscarTexto = ""
                                cantidadInput = "1"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3F98)),
                        modifier = Modifier.height(54.dp),
                        enabled = libroSeleccionado != null && cantidadInput.toIntOrNull() ?: 0 > 0
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Agregar")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Libro", fontSize = 12.sp, color = Color.Gray)
                OutlinedTextField(
                    value = libroSeleccionado?.nombre ?: "Seleccione un libro",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(disabledContainerColor = Color(0xFFF2F4F4))
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Lote", fontSize = 12.sp, color = Color.Gray)
                        var menuLotesExpandido by remember { mutableStateOf(false) }
                        Box {
                            OutlinedTextField(
                                value = loteSeleccionado?.toString() ?: "Seleccione lote",
                                onValueChange = {},
                                readOnly = true,
                                modifier = Modifier.fillMaxWidth(),
                                trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) }
                            )
                            Box(modifier = Modifier.matchParentSize().clickable { if (libroSeleccionado != null) menuLotesExpandido = true })

                            DropdownMenu(expanded = menuLotesExpandido, onDismissRequest = { menuLotesExpandido = false }) {
                                libroSeleccionado?.let { libro ->
                                    val lotes = libro.inventario?.map { it.lote }?.distinct() ?: listOf(libro.lote)
                                    lotes.forEach { lote ->
                                        DropdownMenuItem(
                                            text = { Text("Lote: $lote") },
                                            onClick = {
                                                loteSeleccionado = lote
                                                menuLotesExpandido = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Selector de cantidad
                    Column(modifier = Modifier.weight(0.8f)) {
                        Text("Cantidad", fontSize = 12.sp, color = Color.Gray)
                        OutlinedTextField(
                            value = cantidadInput,
                            onValueChange = { cantidadInput = it },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Cálculo dinámico de Subtotal por Línea de Entrada
                val subtotalPrevio = (libroSeleccionado?.valorVentaPublico ?: 0.0) * (cantidadInput.toIntOrNull() ?: 0)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row {
                        Text("Subtotal ", fontSize = 16.sp, color = Color.Gray)
                        Text(formatoMoneda.format(subtotalPrevio), fontSize = 18.sp, color = Color(0xFFC0392B), fontWeight = FontWeight.Bold)
                    }

                    if (libroSeleccionado != null) {
                        TextButton(
                            onClick = {
                                libroSeleccionado = null
                                loteSeleccionado = null
                                buscarTexto = ""
                                cantidadInput = "1"
                            },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Eliminar item", fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        if (itemsAgregados.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Detalle del Carrito", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A5C))
                    Spacer(modifier = Modifier.height(8.dp))
                    itemsAgregados.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.nombre, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                Text("Lote: ${item.lote} | Cantidad: ${item.cantidad}", fontSize = 12.sp, color = Color.Gray)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(formatoMoneda.format(item.subtotal), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A3A5C))
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(onClick = { itemsAgregados.removeAt(index) }, modifier = Modifier.size(28.dp)) {
                                    Icon(Icons.Default.Delete, "Quitar", tint = Color.Red, modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                        HorizontalDivider(color = Color(0xFFF2F4F4))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total a pagar:", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium)
                Text(formatoMoneda.format(totalVenta), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            }
        }

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = { volverVentas() },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDC3C7)),
                enabled = !estaCargando
            ) {
                Text("Cancelar", color = Color(0xFF1A3A5C), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    if (identificacion.isNotBlank() && itemsAgregados.isNotEmpty()) {
                        val comprobanteFinal = numeroComprobante.ifBlank { "POS-${System.currentTimeMillis() / 1000}" }
                        val notasFinales = observaciones.ifBlank { "Venta POS registrada desde la App Móvil" }

                        val ventaRequest = CrearVentaRequest(
                            identificacionCliente = identificacion,
                            numeroComprobante = comprobanteFinal,
                            observaciones = notasFinales,
                            items = itemsAgregados.map { item ->
                                ItemVentaRequest(
                                    libroId = item.libroId,
                                    lote = item.lote,
                                    cantidad = item.cantidad
                                )
                            }
                        )

                        ventaViewModel.realizarVenta(ventaRequest) {
                            volverVentas()
                        }
                    }
                },
                modifier = Modifier.weight(1f).height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                enabled = !estaCargando && identificacion.isNotBlank() && itemsAgregados.isNotEmpty()
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Registrar Venta", color = Color.White, fontWeight = FontWeight.Bold)
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