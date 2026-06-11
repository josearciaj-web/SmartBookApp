package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Models.Clientes.CrearCliente
import co.edu.cecar.smartbookapp.ViewModel.ClienteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFormularioCliente(
    titulo: String,
    volverClientes: () -> Unit,
    viewModel: ClienteViewModel = viewModel()
) {
    var identificacion by remember { mutableStateOf("") }
    var nombres by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var celular by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }

    // Estados para controlar el calendario flotante
    var mostrarDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    val formateadorFecha = remember {
        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
    }

    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        IconButton(onClick = { volverClientes() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(titulo, color = Color.White, fontSize = 24.sp)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Complete los datos personales del cliente para el registro",
                    color = Color.White,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CampoCliente(
            label = "Identificación *",
            placeholder = "Número de identificación",
            valor = identificacion,
            onChange = { identificacion = it },
            keyboardType = KeyboardType.Number
        )

        CampoCliente(
            label = "Nombres completos *",
            placeholder = "Nombres y apellidos",
            valor = nombres,
            onChange = { nombres = it },
            keyboardType = KeyboardType.Text
        )

        CampoCliente(
            label = "Correo electrónico",
            placeholder = "correo@ejemplo.com",
            valor = correo,
            onChange = { correo = it },
            keyboardType = KeyboardType.Email
        )

        CampoCliente(
            label = "Celular",
            placeholder = "Número de celular",
            valor = celular,
            onChange = { celular = it },
            keyboardType = KeyboardType.Phone
        )

        Text(
            text = "Fecha de nacimiento (DD/MM/YYYY)",
            fontSize = 14.sp,
            color = Color(0xFF1A3A5C),
            modifier = Modifier.padding(top = 10.dp)
        )

        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = { },
            readOnly = true,
            placeholder = { Text("dd/mm/aaaa") },
            trailingIcon = {
                IconButton(onClick = { mostrarDatePicker = true }) {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color(0xFF1A3A5C))
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { mostrarDatePicker = true },
            singleLine = true
        )

        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { volverClientes() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDC3C7)),
                enabled = !estaCargando
            ) {
                Text("Cancelar", color = Color(0xFF1A3A5C))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    val nuevoCliente = CrearCliente(
                        identificacion = identificacion,
                        nombres = nombres,
                        email = correo,
                        celular = celular,
                        fechaNacimiento = fechaNacimiento
                    )

                    viewModel.registrarCliente(nuevoCliente) {
                        volverClientes()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                enabled = !estaCargando && identificacion.isNotBlank() && nombres.isNotBlank()
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Guardar Cliente")
                }
            }
        }
    }

    if (mostrarDatePicker) {
        DatePickerDialog(
            onDismissRequest = { mostrarDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val milisegundos = datePickerState.selectedDateMillis
                        if (milisegundos != null) {
                            fechaNacimiento = formateadorFecha.format(Date(milisegundos))
                        }
                        mostrarDatePicker = false
                    }
                ) {
                    Text("Aceptar", fontWeight = FontWeight.Bold, color = Color(0xFFC0392B))
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDatePicker = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun CampoCliente(
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