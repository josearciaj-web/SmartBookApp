package co.edu.cecar.smartbookapp.Screens

import android.R.drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.People
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
import co.edu.cecar.smartbookapp.ViewModel.ClienteViewModel

@Composable
fun PantallaClientes(
    volverDashboard: () -> Unit,
    irNuevoCliente: () -> Unit,
    irEditarCliente: () -> Unit,
    viewModel: ClienteViewModel = viewModel()
) {
    var buscarState by remember { mutableStateOf("") }
    val clientes = viewModel.listaClientes.filter {
        it.nombres.contains(buscarState, ignoreCase = true) ||
        it.identificacion.contains(buscarState)
    }
    val estaCargando = viewModel.estaCargando

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

        Text(
            text = "Gestión de Clientes",
            fontSize = 30.sp,
            color = Color(0xFF1A3A5C)
        )

        Text(
            text = "Administra los clientes del sistema SmartBook",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = { irNuevoCliente() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B))
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Nuevo Cliente")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = buscarState,
            onValueChange = { buscarState = it },
            placeholder = { Text("Buscar por identificación o nombre...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            },
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
                    Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFF3F3F98))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Listado de Clientes",
                        color = Color(0xFF1A3A5C),
                        fontSize = 20.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Column {
                        FilaEncabezadoClientes()
                        HorizontalDivider()
                        
                        if (estaCargando) {
                            Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        } else if (clientes.isEmpty()) {
                            Text("No se encontraron clientes", modifier = Modifier.padding(20.dp))
                        } else {
                            clientes.forEach { cliente ->
                                FilaCliente(
                                    cliente.identificacion,
                                    cliente.nombres,
                                    cliente.email,
                                    cliente.celular,
                                    irEditarCliente
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
fun FilaEncabezadoClientes() {
    Row(
        modifier = Modifier.width(780.dp)
    ) {
        Text("Identificación", modifier = Modifier.width(130.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Nombre", modifier = Modifier.width(220.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Email", modifier = Modifier.width(230.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Celular", modifier = Modifier.width(110.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Acciones", modifier = Modifier.width(90.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
    }
}

@Composable
fun FilaCliente(
    identificacion: String,
    nombre: String,
    email: String,
    celular: String,
    irEditarCliente: () -> Unit
) {
    Row(
        modifier = Modifier
            .width(780.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(identificacion, modifier = Modifier.width(130.dp), fontSize = 14.sp)
        Text(nombre, modifier = Modifier.width(220.dp), fontSize = 14.sp)
        Text(email, modifier = Modifier.width(230.dp), fontSize = 14.sp)
        Text(celular, modifier = Modifier.width(110.dp), fontSize = 14.sp)
        Text(
            "Editar",
            modifier = Modifier
                .width(90.dp)
                .clickable { irEditarCliente() },
            color = Color(0xFFC0392B),
            fontSize = 14.sp
        )
    }
}