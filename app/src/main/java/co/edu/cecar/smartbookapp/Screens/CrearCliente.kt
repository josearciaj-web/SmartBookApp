package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Models.Clientes.CrearCliente
import co.edu.cecar.smartbookapp.ViewModel.ClienteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearClienteScreen(
    onClienteCreado: () -> Unit = {},
    viewModel: ClienteViewModel = viewModel()
) {
    var identificacionState by remember { mutableStateOf("") }
    var nombresState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var celularState by remember { mutableStateOf("") }
    var fechaNacimientoState by remember { mutableStateOf("") }

    val formularioValido = identificacionState.isNotBlank() && 
                          nombresState.isNotBlank() && 
                          emailState.contains("@")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Cliente", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E2229))
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Registro de Información",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E2229),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Campos de entrada
            CustomTextField("Identificación", identificacionState, Icons.Default.Badge) { identificacionState = it }
            CustomTextField("Nombre Completo", nombresState, Icons.Default.Person) { nombresState = it }
            CustomTextField("Correo Electrónico", emailState, Icons.Default.Email) { emailState = it }
            CustomTextField("Celular", celularState, Icons.Default.Phone) { celularState = it }
            CustomTextField("Fecha Nacimiento (YYYY-MM-DD)", fechaNacimientoState, Icons.Default.DateRange) { fechaNacimientoState = it }

            if (viewModel.mensajeError.isNotEmpty()) {
                Text(
                    text = viewModel.mensajeError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    val nuevoCliente = CrearCliente(
                        identificacion = identificacionState,
                        nombres = nombresState,
                        email = emailState,
                        celular = celularState,
                        fechaNacimiento = fechaNacimientoState
                    )
                    viewModel.registrarCliente(nuevoCliente, onClienteCreado)
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = formularioValido && !viewModel.estaCargando,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
            ) {
                if (viewModel.estaCargando) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("REGISTRAR CLIENTE", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    icon: ImageVector,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFFD32F2F)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFFD32F2F),
            focusedLabelColor = Color(0xFFD32F2F)
        )
    )
}
