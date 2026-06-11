package co.edu.cecar.smartbookapp.Screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import co.edu.cecar.smartbookapp.R
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF3F4F6)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo_cdi),
                contentDescription = "Logo Centro de Idiomas",
                modifier = Modifier
                    .width(220.dp)
                    .height(70.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))


            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // Campo: Correo Electrónico
                    Text(
                        text = "Correo electrónico",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = correoState,
                        onValueChange = { correoState = it },
                        placeholder = { Text("usuario@cecar.edu.co", color = Color(0xFFA3B8D4)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !viewModel.estaCargando,
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF6B7280))
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFEFF6FF), // Tinte azul suave institucional
                            unfocusedContainerColor = Color(0xFFEFF6FF),
                            focusedTextColor = Color(0xFF1F2937),
                            unfocusedTextColor = Color(0xFF1F2937),
                            focusedBorderColor = Color(0xFFD32F2F),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(16.dp))


                    Text(
                        text = "Contraseña",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF374151),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 6.dp)
                    )
                    OutlinedTextField(
                        value = contrasenaState,
                        onValueChange = { contrasenaState = it },
                        placeholder = { Text("••••••••", color = Color(0xFFA3B8D4)) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !viewModel.estaCargando,
                        leadingIcon = {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFF6B7280))
                        },
                        trailingIcon = {
                            IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                                Icon(
                                    imageVector = if (contrasenaVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = Color(0xFF6B7280)
                                )
                            }
                        },
                        visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFFEFF6FF),
                            unfocusedContainerColor = Color(0xFFEFF6FF),
                            focusedTextColor = Color(0xFF1F2937),
                            unfocusedTextColor = Color(0xFF1F2937),
                            focusedBorderColor = Color(0xFFD32F2F),
                            unfocusedBorderColor = Color(0xFFE5E7EB)
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )

                    Spacer(modifier = Modifier.height(14.dp))


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
                            Text("Recordarme", fontSize = 13.sp, color = Color(0xFF4B5563))
                        }

                        Text(
                            text = "¿Olvidaste tu contraseña?",
                            fontSize = 13.sp,
                            color = Color(0xFFD32F2F),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.clickable { navegarAPatallaRestablecerContrasena() }
                        )
                    }


                    if (viewModel.mensajeError.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = viewModel.mensajeError,
                            color = Color.Red,
                            fontSize = 13.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))


                    Button(
                        onClick = {
                            viewModel.autenticarUsuario(
                                usuario = correoState,
                                contrasena = contrasenaState,
                                onSuccess = {
                                    val prefs = context.getSharedPreferences("SmartBook", Context.MODE_PRIVATE)
                                    prefs.edit { putBoolean("Logeado", true) }
                                    navegarAPantallaInicio()
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        enabled = botonHabilitado,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE02B20),
                            disabledContainerColor = Color(0xFFEF4444)
                        )
                    ) {
                        if (viewModel.estaCargando) {
                            CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(text = "Ingresar", fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Text(
            text = "© 2026 SmartBook · Centro de Idiomas CECAR",
            color = Color(0xFF9CA3AF),
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}