package com.example.pmdm01_24_autenticacion

import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch

// --- PALETA DE COLORES ABN ---
val FondoVerde = Color(0xFF6A9B74)
val BotonNaranja = Color(0xFFF29900)
val FondoTarjeta = Color(0xFFEBE3D5)
val TextoVerdeOscuro = Color(0xFF4A614D)

@Composable
fun RegisterScreen(
    onLoginSuccess: (String) -> Unit,
    onMicrosoftClick: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val credentialManager = CredentialManager.create(context)
    val auth = FirebaseAuth.getInstance()

    // --- ESTADOS DE CONTROL ---
    var isLoginMode by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) } // <--- Feedback visual para el tutor

    // Estados de los campos
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Lógica de validación dinámica
    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val passwordsMatch = if (isLoginMode) true else (password == confirmPassword && password.isNotEmpty())
    val isFormValid = if (isLoginMode) {
        isEmailValid && password.length >= 6
    } else {
        name.isNotBlank() && isEmailValid && passwordsMatch && password.length >= 6
    }

    Box(
        modifier = Modifier.fillMaxSize().background(FondoVerde).padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isLoginMode) "¡Hola de nuevo!" else "Registro nuevo usuario",
                fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold
            )
            Text("Gestión de tutor para cálculo infantil", fontSize = 16.sp, color = Color.White)

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = FondoTarjeta),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(0.95f)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = if (isLoginMode) "Inicia sesión" else "Introduce tus datos",
                        fontSize = 20.sp, color = TextoVerdeOscuro, fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isLoginMode) {
                        CustomTextField(name, { name = it }, "Nombre completo", Icons.Default.Person)
                    }

                    CustomTextField(email, { email = it }, "Correo electrónico", Icons.Default.Email, isError = email.isNotEmpty() && !isEmailValid)
                    PasswordField(password, { password = it }, "Contraseña", passwordVisible, { passwordVisible = !passwordVisible })

                    if (!isLoginMode) {
                        PasswordField(confirmPassword, { confirmPassword = it }, "Confirmar contraseña", confirmPasswordVisible, { confirmPasswordVisible = !confirmPasswordVisible }, isError = confirmPassword.isNotEmpty() && !passwordsMatch)
                        if (confirmPassword.isNotEmpty() && !passwordsMatch) {
                            Text("Las contraseñas no coinciden", color = Color.Red, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // BOTÓN PRINCIPAL CON FEEDBACK VISUAL
                    Button(
                        onClick = {
                            if (isFormValid && !isLoading) {
                                isLoading = true
                                if (isLoginMode) {
                                    auth.signInWithEmailAndPassword(email, password)
                                        .addOnSuccessListener {
                                            isLoading = false
                                            onLoginSuccess(it.user?.displayName ?: "Tutor ABN")
                                        }
                                        .addOnFailureListener {
                                            isLoading = false
                                            Toast.makeText(context, "Error: ${it.localizedMessage}", Toast.LENGTH_LONG).show()
                                        }
                                } else {
                                    auth.createUserWithEmailAndPassword(email, password)
                                        .addOnSuccessListener { result ->
                                            val profileUpdates = userProfileChangeRequest { displayName = name }
                                            result.user?.updateProfile(profileUpdates)?.addOnCompleteListener {
                                                isLoading = false
                                                onLoginSuccess(name)
                                            }
                                        }
                                        .addOnFailureListener {
                                            isLoading = false
                                            val errorMsg = if (it.message?.contains("already in use") == true)
                                                "Este correo ya existe. Prueba a Iniciar Sesión." else it.localizedMessage
                                            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                                        }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = !isLoading,
                        colors = ButtonDefaults.buttonColors(containerColor = if (isFormValid && !isLoading) BotonNaranja else Color.Gray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                        } else {
                            Text(if (isLoginMode) "Entrar" else "Regístrate", color = Color.White, fontSize = 18.sp)
                        }
                    }

                    Text(
                        text = if (isLoginMode) "¿No tienes cuenta? Crea una aquí" else "¿Ya tienes cuenta? Inicia sesión",
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp).clickable { isLoginMode = !isLoginMode },
                        color = TextoVerdeOscuro,
                        textDecoration = TextDecoration.Underline,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color.Gray.copy(alpha = 0.3f))
                    Text("o continúa con", modifier = Modifier.align(Alignment.CenterHorizontally).padding(vertical = 8.dp), color = Color.Gray, fontSize = 12.sp)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        SocialButton(R.drawable.ic_google, onClick = {
                            isLoading = true
                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId("970438592020-o4qktgubv5fna7937rc5cl67cdjnucbp.apps.googleusercontent.com")
                                .build()
                            val request = GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
                            scope.launch {
                                try {
                                    val result = credentialManager.getCredential(context, request)
                                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                                    val firebaseCredential = com.google.firebase.auth.GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                                    auth.signInWithCredential(firebaseCredential).addOnSuccessListener {
                                        isLoading = false
                                        onLoginSuccess(it.user?.displayName ?: "Tutor ABN")
                                    }.addOnFailureListener { isLoading = false }
                                } catch (e: Exception) {
                                    isLoading = false
                                    Log.e("AUTH", "Error Google: ${e.message}")
                                }
                            }
                        })
                        Spacer(modifier = Modifier.width(20.dp))
                        SocialButton(R.drawable.ic_microsoft, onClick = onMicrosoftClick)
                    }
                }
            }
        }
    }
}

// --- SUBCOMPONENTES ---
@Composable
fun CustomTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector, isError: Boolean = false) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        leadingIcon = { Icon(icon, null, tint = TextoVerdeOscuro) },
        placeholder = { Text(label) }, isError = isError, singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TextoVerdeOscuro, unfocusedBorderColor = Color.Gray, errorBorderColor = Color.Red)
    )
}

@Composable
fun PasswordField(value: String, onValueChange: (String) -> Unit, label: String, visible: Boolean, onVisibilityChange: () -> Unit, isError: Boolean = false) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        leadingIcon = { Icon(Icons.Default.Lock, null, tint = TextoVerdeOscuro) },
        placeholder = { Text(label) }, isError = isError, singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = { IconButton(onClick = onVisibilityChange) { Icon(if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility, null) } },
        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = TextoVerdeOscuro, unfocusedBorderColor = Color.Gray, errorBorderColor = Color.Red)
    )
}

@Composable
fun SocialButton(iconRes: Int, onClick: () -> Unit) {
    Surface(
        onClick = onClick, shape = RoundedCornerShape(12.dp), color = Color.White,
        shadowElevation = 2.dp, modifier = Modifier.size(54.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = iconRes), contentDescription = null, modifier = Modifier.size(32.dp))
        }
    }
}