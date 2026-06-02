package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Models.Libros.Libro
import co.edu.cecar.smartbookapp.ViewModel.LibroViewModel

@Composable
fun PantallaFormularioLibro(
    idLibro: Int? = null,
    titulo: String,
    volverLibros: () -> Unit,
    viewModel: LibroViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var nivel by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var edicion by remember { mutableStateOf("") }
    var lote by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var valorCompra by remember { mutableStateOf("") }
    var valorVenta by remember { mutableStateOf("") }

    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError
    val esRegistro = idLibro == null || idLibro == 0

    // Cargar datos si es edición
    LaunchedEffect(idLibro) {
        if (!esRegistro && idLibro != null) {
            val libro = viewModel.listaLibros.find { it.id == idLibro }
            if (libro != null) {
                nombre = libro.nombre
                nivel = libro.nivel
                tipo = when (libro.tipo) {
                    1 -> "Student's Book"
                    2 -> "Workbook"
                    3 -> "Texto guía"
                    4 -> "Cuaderno"
                    else -> "Lectura"
                }
                edicion = libro.edicion
                lote = libro.lote.toString()
                stock = libro.unidades.toString()
                valorCompra = libro.valorCompra.toString()
                valorVenta = libro.valorVentaPublico.toString()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        IconButton(onClick = { volverLibros() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row {
                    Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(titulo, color = Color.White, fontSize = 24.sp)
                }

                if (esRegistro) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Completa la información del libro y sus existencias iniciales",
                        color = Color.White,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CampoLibro("Nombre del libro *", "Ingrese nombre del libro", nombre, { nombre = it }, KeyboardType.Text)
        CampoLibro("Nivel *", "Ej: Prejardín, Jardín, Transición", nivel, { nivel = it }, KeyboardType.Text)

        CampoTipoLibro(
            valor = tipo,
            onChange = { tipo = it }
        )

        CampoLibro("Edición *", "Año o edición", edicion, { edicion = it }, KeyboardType.Number)

        if (esRegistro) {
            Spacer(modifier = Modifier.height(12.dp))
            Divider()
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Información de ingresos (opcional)",
                fontSize = 20.sp,
                color = Color(0xFF1A3A5C)
            )

            CampoLibro("Lote *", "Ej: 20261", lote, { lote = it }, KeyboardType.Number)
            CampoLibro("Unidades a ingresar", "Ej: 10", stock, { stock = it }, KeyboardType.Number)
            CampoLibro("Valor de compra", "$ 0.00", valorCompra, { valorCompra = it }, KeyboardType.Number)
            CampoLibro("Valor venta público", "$ 0.00", valorVenta, { valorVenta = it }, KeyboardType.Number)
        }

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { volverLibros() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDC3C7)),
                enabled = !estaCargando
            ) {
                Text("Cancelar", color = Color(0xFF1A3A5C))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    val tipoInt = when(tipo) {
                        "Student's Book" -> 1
                        "Workbook" -> 2
                        "Texto guía" -> 3
                        "Cuaderno" -> 4
                        else -> 5
                    }
                    val nuevoLibro = Libro(
                        id = idLibro ?: 0,
                        nombre = nombre,
                        nivel = nivel,
                        tipo = tipoInt,
                        edicion = edicion,
                        unidades = stock.toIntOrNull() ?: 0,
                        lote = lote.toIntOrNull() ?: 0,
                        valorCompra = valorCompra.toDoubleOrNull() ?: 0.0,
                        valorVentaPublico = valorVenta.toDoubleOrNull() ?: 0.0
                    )
                    viewModel.guardarLibro(nuevoLibro) {
                        volverLibros()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                enabled = !estaCargando
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text("Guardar Libro")
                }
            }
        }
    }
}

@Composable
fun CampoLibro(
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoTipoLibro(
    valor: String,
    onChange: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    val opciones = listOf(
        "Student's Book",
        "Workbook",
        "Texto guía",
        "Cuaderno",
        "Lectura"
    )

    Text(
        text = "Tipo *",
        fontSize = 14.sp,
        color = Color(0xFF1A3A5C),
        modifier = Modifier.padding(top = 10.dp)
    )

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido }
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Seleccione tipo...") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onChange(opcion)
                        expandido = false
                    }
                )
            }
        }
    }
}