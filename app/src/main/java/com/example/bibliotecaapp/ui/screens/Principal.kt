package com.example.bibliotecaapp.ui.screens

import android.media.AudioManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import android.media.ToneGenerator
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.R
import kotlinx.coroutines.launch

class Principal {

    private val tono = ToneGenerator(
        AudioManager.STREAM_NOTIFICATION,
        80
    )
    @Composable


    fun contenidoPrincipal(
        onLoginClick: () -> Unit,
        onSkipLoginClick: () -> Unit,
        onRegistrarClick: () -> Unit,
        snackbarHostState: SnackbarHostState,
        scope: kotlinx.coroutines.CoroutineScope
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.height(35.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.logoprincipal),
                contentDescription = "Logotipo de un gato leyendo",
                modifier = Modifier.height(220.dp)
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Entre Bigote y Letra",
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            Text(
                text = "Para continuar seleccione una opcion",
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(35.dp)
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
                        snackbarHostState.showSnackbar("Vas a la pantalla de login")
                    }
                    onLoginClick()
                }
            ) {
                Text(
                    text = "Login",
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
                        snackbarHostState.showSnackbar("Pasas directo a la biblioteca")
                    }
                    onSkipLoginClick()
                }
            ) {
                Text(
                    text = "Continuar sin login",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(35.dp)
            )

            Text(
                text = "Registrate Aqui!",
                fontSize = 16.sp,
                modifier = Modifier.clickable {

                    tono.startTone(
                        ToneGenerator.TONE_PROP_ACK,
                        300
                    )
                    scope.launch {
                        snackbarHostState.showSnackbar("Te llevamos al registro")
                    }
                    onRegistrarClick()
                }
            )
        }
    }

    @Composable
    @OptIn(ExperimentalMaterial3Api::class)
    fun pantallaInicio(
        onLoginClick: () -> Unit,
        onSkipLoginClick: () -> Unit,
        onRegistrarClick: () -> Unit
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
                    contenidoPrincipal(
                        onLoginClick = onLoginClick,
                        onSkipLoginClick = onSkipLoginClick,
                        onRegistrarClick = onRegistrarClick,
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )
                }
            }
        }
    }
}