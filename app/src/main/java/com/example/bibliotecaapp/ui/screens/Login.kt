package com.example.bibliotecaapp.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.R
import com.example.bibliotecaapp.data.usuarios
import kotlinx.coroutines.launch

class Login {


    private val tono = ToneGenerator(
        AudioManager.STREAM_NOTIFICATION,
        80
    )
    @Composable
    fun contenidoLogin(
        onIngresarClick: (String) -> Unit,
        onVolverClick: () -> Unit,
        snackbarHostState: SnackbarHostState,
        scope: kotlinx.coroutines.CoroutineScope
    ) {

        var usuario by remember {
            mutableStateOf("")
        }

        var clave by remember {
            mutableStateOf("")
        }

        var mensajeError by remember {
            mutableStateOf("")
        }

        fun validarUsuario(): Boolean {
            for (u in usuarios) {
                if (u.usuario == usuario && u.clave == clave) {
                    return true
                }
            }
            return false
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logoprincipal),
                contentDescription = "Logotipo de un gato leyendo",
                modifier = Modifier.height(220.dp)
            )

            Spacer(
                modifier = Modifier.height(115.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = usuario,
                    onValueChange = {
                        usuario = it
                        mensajeError = ""
                    },
                    label = {
                        Text("Usuario")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(15.dp)
                )

                OutlinedTextField(
                    value = clave,
                    onValueChange = {
                        clave = it
                        mensajeError = ""
                    },
                    label = {
                        Text("Contraseña")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(
                    modifier = Modifier.height(5.dp)
                )

                if (mensajeError.isNotEmpty()) {
                    Text(
                        text = mensajeError,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(
                    modifier = Modifier.height(30.dp)
                )
            }

            Button(
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                onClick = {
                    if (usuario.isEmpty() || clave.isEmpty()) {
                        mensajeError = "Complete todos los campos"
                        scope.launch {
                            snackbarHostState.showSnackbar("Complete todos los campos")
                        }
                    } else if (validarUsuario()) {
                        tono.startTone(
                            ToneGenerator.TONE_PROP_ACK,
                            300
                        )
                        mensajeError = ""
                        scope.launch {
                            snackbarHostState.showSnackbar("Bienvenido $usuario")
                        }
                        onIngresarClick(usuario)
                    } else {
                        mensajeError = "Error de usuario o contraseña"
                        scope.launch {
                            snackbarHostState.showSnackbar("Error de usuario o contraseña")
                        }
                    }
                }
            ) {
                Text(
                    text = "Ingresar",
                    fontSize = 20.sp
                )
            }

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Button(
                modifier = Modifier
                    .width(300.dp)
                    .height(50.dp),
                onClick = {
                    tono.startTone(
                        ToneGenerator.TONE_PROP_ACK,
                        300
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar("Volviendo a la pantalla principal")
                    }
                    onVolverClick()
                }
            ) {
                Text(
                    text = "Volver",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(15.dp)
            )

            Text(
                text = "Olvido su contraseña?, click AQUI",
                fontSize = 13.sp,
                modifier = Modifier.clickable {
                    scope.launch {
                        snackbarHostState.showSnackbar("Funcionalidad en desarrollo")
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun pantallaLogin(
        onIngresarClick: (String) -> Unit,
        onVolverClick: () -> Unit
    ) {

        val snackbarHostState = remember {
            SnackbarHostState()
        }

        val scope = rememberCoroutineScope()

        MaterialTheme(
            colorScheme = lightColorScheme(
                primary = Color(0xFFBA9165),
                secondary = Color(0xFFFF9800),
                onPrimary = Color.White
            )
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("Biblioteca Online")
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                },
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackbarHostState
                    )
                }
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    contenidoLogin(
                        onIngresarClick = onIngresarClick,
                        onVolverClick = onVolverClick,
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )
                }
            }
        }
    }
}