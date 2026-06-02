package co.edu.cecar.smartbookapp.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import co.edu.cecar.smartbookapp.ViewModel.UsuarioViewModel

@Composable
fun PantallaUsuarios(
    volverDashboard: () -> Unit,
    irNuevoUsuario: () -> Unit,
    irEditarUsuario: (Int) -> Unit,
    viewModel: UsuarioViewModel = viewModel()
) {
    val listaUsuarios = viewModel.listaUsuarios
    val estaCargando = viewModel.estaCargando

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
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
                modifier = Modifier.width(145.dp).height(55.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Gestión de Usuarios", fontSize = 28.sp, color = Color(0xFF1A3A5C))
        Text("Administración de accesos al sistema", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { irNuevoUsuario() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nuevo Usuario")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column {
                Text(
                    text = "Lista de Usuarios",
                    fontSize = 20.sp,
                    color = Color(0xFF1A3A5C),
                    modifier = Modifier.padding(14.dp)
                )

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Column {
                        FilaEncabezadoUsuarios()
                        HorizontalDivider()

                        if (estaCargando) {
                            Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = Color(0xFFC0392B))
                            }
                        } else if (viewModel.mensajeError.isNotEmpty()) {
                            Text(viewModel.mensajeError, color = Color.Red, modifier = Modifier.padding(10.dp))
                        } else if (listaUsuarios.isEmpty()) {
                            Text("No hay usuarios registrados.", modifier = Modifier.padding(10.dp))
                        } else {
                            listaUsuarios.forEach { usuario ->
                                FilaUsuario(
                                    id = usuario.id ?: 0,
                                    nombre = usuario.nombreUsuario,
                                    correo = usuario.correo,
                                    rol = usuario.rol,
                                    estado = if (usuario.activo) "Activo" else "Inactivo",
                                    irEditarUsuario = irEditarUsuario,
                                    onEliminar = { viewModel.eliminarUsuario(usuario.id ?: 0) }
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
fun FilaEncabezadoUsuarios() {
    Row(modifier = Modifier.width(800.dp)) {
        Text("Usuario", modifier = Modifier.width(200.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Correo", modifier = Modifier.width(250.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Rol", modifier = Modifier.width(150.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Estado", modifier = Modifier.width(100.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Acciones", modifier = Modifier.width(100.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
    }
}

@Composable
fun FilaUsuario(
    id: Int,
    nombre: String,
    correo: String,
    rol: String,
    estado: String,
    irEditarUsuario: (Int) -> Unit,
    onEliminar: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(800.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(nombre, modifier = Modifier.width(200.dp), fontSize = 14.sp)
        Text(correo, modifier = Modifier.width(250.dp), fontSize = 14.sp)
        Text(rol, modifier = Modifier.width(150.dp), fontSize = 14.sp)
        Text(
            estado,
            modifier = Modifier.width(100.dp),
            fontSize = 14.sp,
            color = if (estado == "Activo") Color(0xFF27AE60) else Color.Red
        )
        Row(modifier = Modifier.width(100.dp)) {
            Icon(
                Icons.Default.Edit,
                contentDescription = "Editar",
                tint = Color(0xFF3F3F98),
                modifier = Modifier.size(20.dp).clickable { irEditarUsuario(id) }
            )
            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = Color(0xFFC0392B),
                modifier = Modifier.size(20.dp).clickable { onEliminar() }
            )
        }
    }
}
