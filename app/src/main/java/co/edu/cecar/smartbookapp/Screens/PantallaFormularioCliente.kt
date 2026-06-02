package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Models.Clientes.CrearCliente
import co.edu.cecar.smartbookapp.ViewModel.ClienteViewModel

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
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column {

                Text(
                    text = titulo,
                    color = Color.White,
                    fontSize = 24.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp)
                        .height(70.dp)
                        .padding(start = 20.dp, top = 20.dp)
                )

                Divider(color = Color(0xFFC0392B), thickness = 70.dp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CampoCliente(
            label = "Identificación",
            placeholder = "Número de identificación",
            valor = identificacion,
            onChange = { identificacion = it },
            keyboardType = KeyboardType.Number
        )

        CampoCliente(
            label = "Nombres completos",
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
            text = "Fecha de nacimiento",
            fontSize = 14.sp,
            color = Color(0xFF1A3A5C),
            modifier = Modifier.padding(top = 10.dp)
        )

        OutlinedTextField(
            value = fechaNacimiento,
            onValueChange = { fechaNacimiento = it },
            placeholder = { Text("dd/mm/aaaa") },
            trailingIcon = {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }

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
                enabled = !estaCargando
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Text("Guardar Cliente")
                }
            }
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