package co.edu.cecar.smartbookapp.Screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.edu.cecar.smartbookapp.R

@Composable
fun PantallaInventario(
    volverDashboard: () -> Unit
) {
    var loteSeleccionado by remember { mutableStateOf("Ver Todo") }
    var loteBuscar by remember { mutableStateOf("20261") }

    val libros = when (loteSeleccionado) {
        "Sem 2 · 2025" -> listOf(
            InventarioLibro("Passages 1", "Teens C1.1", "1ra", "StudentsBook", "20252", "500", "16", "484"),
            InventarioLibro("Superminds 6", "Kids A2.3", "1ra", "StudentsBook", "20252", "100", "1", "99")
        )

        "Sem 1 · 2026" -> listOf(
            InventarioLibro("Passages 1", "Teens C1.1", "1ra", "StudentsBook", "20261", "500", "16", "484"),
            InventarioLibro("Superminds 6", "Kids A2.3", "1ra", "StudentsBook", "20261", "100", "1", "99"),
            InventarioLibro("Superminds 2", "Kids A1.2", "1", "StudentsBook", "20261", "100", "1", "99"),
            InventarioLibro("Uncover 3A", "Teens B1.1", "1ra", "StudentsBook", "20261", "80", "12", "68")
        )

        "Sem 2 · 2026" -> listOf(
            InventarioLibro("Uncover 5C", "Teens", "2", "StudentsBook", "20262", "50", "0", "50"),
            InventarioLibro("Uncover 6A", "1", "3ra", "StudentsBook", "20262", "60", "0", "60")
        )

        else -> listOf(
            InventarioLibro("Passages 1", "Teens C1.1", "1ra", "StudentsBook", "20261", "500", "16", "484"),
            InventarioLibro("Superminds 6", "Kids A2.3", "1ra", "StudentsBook", "20261", "100", "1", "99"),
            InventarioLibro("Superminds 2", "Kids A1.2", "1", "StudentsBook", "20261", "100", "1", "99"),
            InventarioLibro("Uncover 3A", "Teens B1.1", "1ra", "StudentsBook", "20261", "80", "12", "68")
        )
    }

    val stockTotal = libros.sumOf { it.stockDisponible.toInt() }
    val bajoStock = libros.count { it.stockDisponible.toInt() <= 10 }

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

        Text("Gestión de Inventario", fontSize = 30.sp, color = Color(0xFF1A3A5C))
        Text("Control y seguimiento del stock de libros", fontSize = 14.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                loteSeleccionado = "Ver Todo"
                loteBuscar = ""
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3F98))
        ) {
            Icon(Icons.Default.FilterList, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ver Todo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            TarjetaInventario("Total Libros", "24", Icons.Default.MenuBook, Color(0xFFC0392B), Modifier.weight(1f))
            Spacer(modifier = Modifier.width(10.dp))
            TarjetaInventario("Bajo Stock", bajoStock.toString(), Icons.Default.Warning, Color(0xFF3F3F98), Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(10.dp))

        TarjetaInventario("Stock Total", stockTotal.toString(), Icons.Default.Inventory, Color(0xFFC0392B), Modifier.fillMaxWidth())

        Spacer(modifier = Modifier.height(18.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {

                Text("FILTRAR POR LOTE", fontSize = 14.sp, color = Color(0xFF1A3A5C))

                Spacer(modifier = Modifier.height(10.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = loteBuscar,
                        onValueChange = { loteBuscar = it },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            loteSeleccionado = when (loteBuscar) {
                                "20252" -> "Sem 2 · 2025"
                                "20261" -> "Sem 1 · 2026"
                                "20262" -> "Sem 2 · 2026"
                                else -> "Ver Todo"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F3F98))
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Buscar")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    BotonLote("Sem 2 · 2025", loteSeleccionado) {
                        loteSeleccionado = "Sem 2 · 2025"
                        loteBuscar = "20252"
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    BotonLote("Sem 1 · 2026", loteSeleccionado) {
                        loteSeleccionado = "Sem 1 · 2026"
                        loteBuscar = "20261"
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    BotonLote("Sem 2 · 2026", loteSeleccionado) {
                        loteSeleccionado = "Sem 2 · 2026"
                        loteBuscar = "20262"
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Formato: año + semestre — ej: 20261",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

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
                    Icon(Icons.Default.Inventory, contentDescription = null, tint = Color(0xFF3F3F98))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Inventario de Libros", color = Color(0xFF1A3A5C), fontSize = 20.sp)
                }

                Row(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(14.dp)
                ) {
                    Column {
                        FilaEncabezadoInventario()
                        Divider()

                        libros.forEach { libro ->
                            FilaInventario(libro)
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaInventario(
    titulo: String,
    valor: String,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    colorSuperior: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(120.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            Divider(color = colorSuperior, thickness = 4.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(titulo, fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(valor, fontSize = 24.sp, color = Color(0xFF1A3A5C))
                }

                Icon(icono, contentDescription = null, tint = colorSuperior, modifier = Modifier.size(32.dp))
            }
        }
    }
}

@Composable
fun BotonLote(
    texto: String,
    loteSeleccionado: String,
    onClick: () -> Unit
) {
    val seleccionado = texto == loteSeleccionado

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (seleccionado) Color(0xFF3F3F98) else Color(0xFFEFEFEF)
        )
    ) {
        Text(
            text = texto,
            color = if (seleccionado) Color.White else Color(0xFF1A3A5C),
            fontSize = 13.sp
        )
    }
}

@Composable
fun FilaEncabezadoInventario() {
    Row(modifier = Modifier.width(1150.dp)) {
        Text("Libro", modifier = Modifier.width(230.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Nivel", modifier = Modifier.width(180.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Edición", modifier = Modifier.width(90.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Tipo", modifier = Modifier.width(160.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Lote", modifier = Modifier.width(100.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Ingresada", modifier = Modifier.width(120.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Vendida", modifier = Modifier.width(110.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
        Text("Stock", modifier = Modifier.width(110.dp), color = Color(0xFF1A3A5C), fontSize = 15.sp)
    }
}

@Composable
fun FilaInventario(libro: InventarioLibro) {
    Row(
        modifier = Modifier
            .width(1150.dp)
            .padding(vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(libro.nombre, modifier = Modifier.width(230.dp), fontSize = 14.sp)
        Text(libro.nivel, modifier = Modifier.width(180.dp), fontSize = 14.sp)
        Text(libro.edicion, modifier = Modifier.width(90.dp), fontSize = 14.sp)
        Text(libro.tipo, modifier = Modifier.width(160.dp), fontSize = 14.sp)
        Text(libro.lote, modifier = Modifier.width(100.dp), fontSize = 14.sp)
        Text(libro.cantidadIngresada, modifier = Modifier.width(120.dp), fontSize = 14.sp)
        Text(libro.cantidadVendida, modifier = Modifier.width(110.dp), fontSize = 14.sp)

        val stock = libro.stockDisponible.toInt()
        Text(
            text = libro.stockDisponible,
            modifier = Modifier.width(110.dp),
            color = if (stock <= 10) Color(0xFFC0392B) else Color(0xFF1A3A5C),
            fontSize = 14.sp
        )
    }
}

data class InventarioLibro(
    val nombre: String,
    val nivel: String,
    val edicion: String,
    val tipo: String,
    val lote: String,
    val cantidadIngresada: String,
    val cantidadVendida: String,
    val stockDisponible: String
)