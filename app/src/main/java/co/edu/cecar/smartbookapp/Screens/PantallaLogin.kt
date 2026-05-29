package co.edu.cecar.smartbookapp.Screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import co.edu.cecar.smartbookapp.ViewModel.LoginViewModel

@Composable
fun PantallaLogin(
    navegarAPantallaInicio: () -> Unit,
    navegarAPatallaRestablecerContrasena: () -> Unit,
    viewModel: LoginViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var correoState by remember { mutableStateOf("") }
    var contrasenaState by remember { mutableStateOf("") }
    var recordarMeState by remember { mutableStateOf(false) }
    var contrasenaVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val botonHabilitado =
        correoState.isNotBlank() && contrasenaState.isNotBlank() && !viewModel.estaCargando

    // Contenedor externo (Fondo oscuro transparente simulando la imagen)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E2229)), // Color oscuro de fondo
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {

            // Tarjeta Blanca Central
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // LOGO (Reemplaza el texto por tu Image cuando la agregues a drawables)
                    // Image(painter = painterResource(id = R.drawable.logo_cdi), contentDescription = "Logo")
                    Text(
                        text = "CDI CENTRO DE IDIOMAS",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F), // Rojo institucional
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "CECAR",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Light,
                        color = Color(0xFF0B3A5B),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    // Campo: Correo Electrónico
                    Text(
                        text = "Correo electrónico",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = correoState,
                        onValueChange = { correoState = it },
                        placeholder = { Text("usuario@cecar.edu.co", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !viewModel.estaCargando,
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color.Gray)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD32F2F),
                            unfocusedBorderColor = Color.LightGray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Campo: Contraseña
                    Text(
                        text = "Contraseña",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = contrasenaState,
                        onValueChange = { contrasenaState = it },
                        placeholder = { Text("••••••••", color = Color.LightGray) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !viewModel.estaCargando,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.Gray)
                        },
                        trailingIcon = {
                            IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                                Icon(
                                    imageVector = if (contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color.Gray
                                )
                            }
                        },
                        visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFD32F2F),
                            unfocusedBorderColor = Color.LightGray
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    // Fila de Recordarme y Olvidé mi contraseña
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = recordarMeState,
                                onCheckedChange = { recordarMeState = it },
                                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFD32F2F)),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Recordarme", fontSize = 13.sp, color = Color(0xFF555555))
                        }

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 13.sp,
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { navegarAPatallaRestablecerContrasena() }
                        )
                    }

                    // Manejo de Errores de la API
                    if (viewModel.mensajeError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = viewModel.mensajeError,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón de Ingresar
                    Button(
                        onClick = {
                            viewModel.autenticarUsuario(
                                usuario = correoState,
                                contrasena = contrasenaState,
                                onSuccess = {
                                    val prefs = context.getSharedPreferences(
                                        "SmartBook",
                                        Context.MODE_PRIVATE
                                    )
                                    prefs.edit {
                                        putBoolean("Logeado", true)
                                    }
                                    navegarAPantallaInicio()
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        enabled = botonHabilitado,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD32F2F), // Rojo exacto del botón
                            disabledContainerColor = Color(0xFFE57373)
                        )
                    ) {
                        Text(
                            text = if (viewModel.estaCargando) "Verificando..." else "Ingresar",
                            fontSize = 16.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // Pie de página (Footer) debajo de la tarjeta flotante
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "© 2026 SmartBook · CDI CECAR",
                color = Color.LightGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
