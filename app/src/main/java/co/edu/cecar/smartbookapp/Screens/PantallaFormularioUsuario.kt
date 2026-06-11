package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import co.edu.cecar.smartbookapp.Models.Usuario.Usuario
import co.edu.cecar.smartbookapp.ViewModel.UsuarioViewModel

@Composable
fun PantallaFormularioUsuario(
    idUsuario: Int? = null,
    titulo: String,
    volverUsuarios: () -> Unit,
    viewModel: UsuarioViewModel = viewModel()
) {
    var identificacion by remember { mutableStateOf("") }
    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }

    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError
    val esRegistro = idUsuario == null || idUsuario == 0

    LaunchedEffect(idUsuario) {
        if (!esRegistro && idUsuario != null) {
            val usuario = viewModel.listaUsuarios.find { it.id == idUsuario }
            if (usuario != null) {
                identificacion = usuario.identificacion
                nombreUsuario = usuario.nombreUsuario
                correo = usuario.correo
                rol = usuario.rol
                estado = if (usuario.activo) "Activo" else "Inactivo"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        IconButton(onClick = { volverUsuarios() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE52B2E)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Row(modifier = Modifier.padding(20.dp)) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(10.dp))
                Text(titulo, color = Color.White, fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        CampoUsuario("Identificación", "Número de identificación", identificacion, { identificacion = it }, KeyboardType.Number)
        CampoUsuario("Nombres completos", "Nombres y apellidos", nombreUsuario, { nombreUsuario = it })
        CampoUsuario("Correo electrónico", "correo@ejemplo.com", correo, { correo = it }, KeyboardType.Email)

        if (esRegistro) {
            CampoUsuario("Contraseña", "Contraseña segura", contrasena, { contrasena = it }, KeyboardType.Password)
            Text("Mínimo 6 caracteres", color = Color.Gray, fontSize = 13.sp)
        }

        CampoDropdownUsuario(
            label = "Rol",
            valor = rol,
            placeholder = "Seleccione un rol",
            opciones = listOf("Administrador", "Vendedor"),
            onChange = { rol = it }
        )

        CampoDropdownUsuario(
            label = "Estado",
            valor = estado,
            placeholder = "Seleccione un estado",
            opciones = listOf("Activo", "Inactivo"),
            onChange = { estado = it }
        )

        if (mensajeError.isNotEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(mensajeError, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = { volverUsuarios() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCED4DA)),
                enabled = !estaCargando
            ) {
                Text("Cancelar", color = Color(0xFF1A3A5C))
            }

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    val usuario = Usuario(
                        id = if (esRegistro) null else idUsuario,
                        identificacion = identificacion,
                        nombreUsuario = nombreUsuario,
                        correo = correo,
                        password = contrasena,
                        rol = rol,
                        activo = estado == "Activo"
                    )

                    viewModel.guardarUsuario(usuario) {
                        volverUsuarios()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE52B2E)),
                enabled = !estaCargando &&
                        identificacion.isNotBlank() &&
                        nombreUsuario.isNotBlank() &&
                        correo.isNotBlank() &&
                        rol.isNotBlank() &&
                        estado.isNotBlank() &&
                        (!esRegistro || contrasena.length >= 6)
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text("Guardar Usuario", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun CampoUsuario(
    label: String,
    placeholder: String,
    valor: String,
    onChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Text(
        text = label,
        fontSize = 15.sp,
        color = Color(0xFF374151),
        modifier = Modifier.padding(top = 14.dp, bottom = 6.dp)
    )

    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        placeholder = { Text(placeholder, color = Color(0xFF9CA3AF)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CampoDropdownUsuario(
    label: String,
    valor: String,
    placeholder: String,
    opciones: List<String>,
    onChange: (String) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }

    Text(
        text = label,
        fontSize = 15.sp,
        color = Color(0xFF374151),
        modifier = Modifier.padding(top = 14.dp, bottom = 6.dp)
    )

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = !expandido }
    ) {
        OutlinedTextField(
            value = valor,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text(placeholder) },
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