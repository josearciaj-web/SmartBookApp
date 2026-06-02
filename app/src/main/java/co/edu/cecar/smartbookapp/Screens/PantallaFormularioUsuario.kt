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
    var nombreUsuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    val estaCargando = viewModel.estaCargando
    val mensajeError = viewModel.mensajeError
    val esRegistro = idUsuario == null || idUsuario == 0

    LaunchedEffect(idUsuario) {
        if (!esRegistro && idUsuario != null) {
            val usuario = viewModel.listaUsuarios.find { it.id == idUsuario }
            if (usuario != null) {
                nombreUsuario = usuario.nombreUsuario
                correo = usuario.correo
                rol = usuario.rol
                activo = usuario.activo
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
            colors = CardDefaults.cardColors(containerColor = Color(0xFFC0392B)),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(titulo, color = Color.White, fontSize = 24.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        CampoTexto("Nombre de Usuario *", "Ej: admin_smart", nombreUsuario, { nombreUsuario = it })
        CampoTexto("Correo Electrónico *", "ejemplo@cecar.edu.co", correo, { correo = it }, KeyboardType.Email)
        
        CampoRol(rol = rol, onChange = { rol = it })

        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically, modifier = Modifier.padding(top = 10.dp)) {
            Checkbox(checked = activo, onCheckedChange = { activo = it })
            Text("Usuario Activo")
        }

        if (mensajeError.isNotEmpty()) {
            Text(mensajeError, color = Color.Red, modifier = Modifier.padding(vertical = 8.dp))
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(
                onClick = { volverUsuarios() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBDC3C7)),
                enabled = !estaCargando
            ) {
                Text("Cancelar", color = Color(0xFF1A3A5C))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    val usuario = Usuario(
                        id = idUsuario,
                        nombreUsuario = nombreUsuario,
                        correo = correo,
                        rol = rol,
                        activo = activo
                    )
                    viewModel.guardarUsuario(usuario) {
                        volverUsuarios()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
                enabled = !estaCargando && nombreUsuario.isNotBlank() && correo.isNotBlank() && rol.isNotBlank()
            ) {
                if (estaCargando) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                } else {
                    Text("Guardar Usuario")
                }
            }
        }
    }
}

@Composable
fun CampoTexto(label: String, placeholder: String, valor: String, onChange: (String) -> Unit, keyboardType: KeyboardType = KeyboardType.Text) {
    Text(text = label, fontSize = 14.sp, color = Color(0xFF1A3A5C), modifier = Modifier.padding(top = 10.dp))
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
fun CampoRol(rol: String, onChange: (String) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    val opciones = listOf("Admin", "Vendedor", "Cajero")

    Text(text = "Rol *", fontSize = 14.sp, color = Color(0xFF1A3A5C), modifier = Modifier.padding(top = 10.dp))
    ExposedDropdownMenuBox(expanded = expandido, onExpandedChange = { expandido = !expandido }) {
        OutlinedTextField(
            value = rol,
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Seleccione rol...") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.fillMaxWidth().menuAnchor()
        )
        ExposedDropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            opciones.forEach { opcion ->
                DropdownMenuItem(text = { Text(opcion) }, onClick = { onChange(opcion); expandido = false })
            }
        }
    }
}
