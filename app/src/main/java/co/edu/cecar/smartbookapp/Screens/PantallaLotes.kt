package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smartbookapp.R

@Composable
fun PantallaLotes(
    volverDashboard: () -> Unit
) {
    var buscarState by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { volverDashboard() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color(0xFF1A3A5C)
                )
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
            text = "Gestión de Lotes",
            fontSize = 30.sp,
            color = Color(0xFF1A3A5C)
        )

        Text(
            text = "Administra los lotes del sistema SmartBook",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {},
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
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray
                )
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
                    Icon(
                        Icons.Default.Inventory,
                        contentDescription = null,
                        tint = Color(0xFF3F3F98)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Listado de Lotes",
                        color = Color(0xFF1A3A5C),
                        fontSize = 20.sp
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                ) {
                    Text(
                        text = "Lote",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF1A3A5C),
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Actual",
                        modifier = Modifier.weight(1f),
                        color = Color(0xFF1A3A5C),
                        fontSize = 16.sp
                    )
                }

                Divider()

                FilaLote("20261", "Sí", Color(0xFF1BAA4B))
                Divider()
                FilaLote("20252", "No", Color(0xFFC0392B))
            }
        }
    }
}

@Composable
fun FilaLote(
    lote: String,
    actual: String,
    colorEstado: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = lote,
            modifier = Modifier.weight(1f),
            fontSize = 16.sp,
            color = Color.Black
        )

        AssistChip(
            onClick = {},
            label = { Text(actual) },
            colors = AssistChipDefaults.assistChipColors(
                labelColor = colorEstado
            )
        )
    }
}