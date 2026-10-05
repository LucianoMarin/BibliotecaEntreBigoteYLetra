package com.example.bibliotecaapp.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.data.sqlite.BibliotecaSQLite
import kotlinx.coroutines.launch

private val tono = ToneGenerator(
    AudioManager.STREAM_NOTIFICATION,
    80
)

@Composable
fun contenidoDatosPersonales(
    bibliotecaSQLite: BibliotecaSQLite,
    onContinuarClick: (
        rut: String,
        pNombre: String,
        sNombre: String,
        aPaterno: String,
        aMaterno: String
    ) -> Unit,
    onVolverClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope
) {

    var rut by remember { mutableStateOf("") }
    var primer_nombre by remember { mutableStateOf("") }
    var segundo_nombre by remember { mutableStateOf("") }
    var apellido_paterno by remember { mutableStateOf("") }
    var apellido_materno by remember { mutableStateOf("") }

    var errorRut by remember { mutableStateOf("") }
    var errorPrimerNombre by remember { mutableStateOf("") }
    var errorSegundoNombre by remember { mutableStateOf("") }
    var errorApellidoPaterno by remember { mutableStateOf("") }
    var errorApellidoMaterno by remember { mutableStateOf("") }

    var mensajeError by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }

    fun validarFormulario(): Boolean {

        errorRut = ""
        errorPrimerNombre = ""
        errorSegundoNombre = ""
        errorApellidoPaterno = ""
        errorApellidoMaterno = ""
        mensajeError = ""
        mensajeExito = ""

        var valido = true

        if (rut.isEmpty()) {

            errorRut = "Error en campo Rut"
            valido = false

        } else if (rut.length < 8) {

            errorRut = "Error en campo Rut"
            valido = false

        } else {

            if (bibliotecaSQLite.existeRut(rut)) {

                errorRut = "El RUT ya está registrado"
                valido = false
            }
        }

        if (primer_nombre.isEmpty()) {

            errorPrimerNombre = "Error en campo Primer Nombre"
            valido = false

        } else {

            for (letra in primer_nombre) {

                if (!letra.isLetter() && letra != ' ') {

                    errorPrimerNombre = "Error en campo Primer Nombre"
                    valido = false
                }
            }
        }

        if (segundo_nombre.isNotEmpty()) {

            for (letra in segundo_nombre) {

                if (!letra.isLetter() && letra != ' ') {

                    errorSegundoNombre = "Error en campo Segundo Nombre"
                    valido = false
                }
            }
        }

        if (apellido_paterno.isEmpty()) {

            errorApellidoPaterno = "Error en campo Apellido Paterno"
            valido = false

        } else {

            for (letra in apellido_paterno) {

                if (!letra.isLetter() && letra != ' ') {

                    errorApellidoPaterno = "Error en campo Apellido Paterno"
                    valido = false
                }
            }
        }

        if (apellido_materno.isEmpty()) {

            errorApellidoMaterno = "Error en campo Apellido Materno"
            valido = false

        } else {

            for (letra in apellido_materno) {

                if (!letra.isLetter() && letra != ' ') {

                    errorApellidoMaterno = "Error en campo Apellido Materno"
                    valido = false
                }
            }
        }

        return valido
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(15.dp))

        LogoAplicacion(
            ContenidoBajoImagen = {

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Datos Personales",
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        )

        Spacer(modifier = Modifier.height(25.dp))

        OutlinedTextField(
            value = rut,
            onValueChange = {
                rut = it
                errorRut = ""
                mensajeError = ""
                mensajeExito = ""
            },
            label = {
                Text("Rut")
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorRut.isNotEmpty()) {

            Text(
                text = errorRut,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = primer_nombre,
            onValueChange = {
                primer_nombre = it
                errorPrimerNombre = ""
                mensajeError = ""
                mensajeExito = ""
            },
            label = {
                Text("Primer Nombre")
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorPrimerNombre.isNotEmpty()) {

            Text(
                text = errorPrimerNombre,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = segundo_nombre,
            onValueChange = {
                segundo_nombre = it
                errorSegundoNombre = ""
                mensajeError = ""
                mensajeExito = ""
            },
            label = {
                Text("Segundo Nombre")
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorSegundoNombre.isNotEmpty()) {

            Text(
                text = errorSegundoNombre,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = apellido_paterno,
            onValueChange = {
                apellido_paterno = it
                errorApellidoPaterno = ""
                mensajeError = ""
                mensajeExito = ""
            },
            label = {
                Text("Apellido Paterno")
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorApellidoPaterno.isNotEmpty()) {

            Text(
                text = errorApellidoPaterno,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        OutlinedTextField(
            value = apellido_materno,
            onValueChange = {
                apellido_materno = it
                errorApellidoMaterno = ""
                mensajeError = ""
                mensajeExito = ""
            },
            label = {
                Text("Apellido Materno")
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (errorApellidoMaterno.isNotEmpty()) {

            Text(
                text = errorApellidoMaterno,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
        Spacer(modifier = Modifier.height(5.dp))

        if (mensajeError.isNotEmpty()) {

            Text(
                text = mensajeError,
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (mensajeExito.isNotEmpty()) {

            Text(
                text = mensajeExito,
                color = Color.Green,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),

            onClick = {

                if (validarFormulario()) {

                    tono.startTone(
                        ToneGenerator.TONE_PROP_ACK,
                        300
                    )

                    mensajeExito = "Datos válidos"
                    mensajeError = ""

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Datos válidos, continuando..."
                        )
                    }

                    onContinuarClick(
                        rut,
                        primer_nombre,
                        segundo_nombre,
                        apellido_paterno,
                        apellido_materno
                    )

                } else {

                    mensajeError = "Complete los campos con error"

                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Complete los campos con error"
                        )
                    }
                }
            }
        ) {

            Text(
                text = "Continuar",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

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
                    snackbarHostState.showSnackbar(
                        "Volviendo a la pantalla principal"
                    )
                }

                onVolverClick()
            }
        ) {

            Text(
                text = "INICIO",
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pantallaDatosPersonales(
    bibliotecaSQLite: BibliotecaSQLite,
    onContinuarClick: (
        rut: String,
        pNombre: String,
        sNombre: String,
        aPaterno: String,
        aMaterno: String
    ) -> Unit,
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

                contenidoDatosPersonales(
                    bibliotecaSQLite = bibliotecaSQLite,
                    onContinuarClick = onContinuarClick,
                    onVolverClick = onVolverClick,
                    snackbarHostState = snackbarHostState,
                    scope = scope
                )
            }
        }
    }
}