package com.example.bibliotecaapp.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.data.Usuario
import com.example.bibliotecaapp.data.sqlite.BibliotecaSQLite
import com.example.bibliotecaapp.ui.screens.com.example.bibliotecaapp.data.Socio
import kotlinx.coroutines.launch

/*
* CONFIGURACION DE SONIDO PREDETERMINADO
* AVISO SONORO PARA ACCIONES DE BOTON
* */

private val tono = ToneGenerator(
    AudioManager.STREAM_NOTIFICATION,
    80
)

@Composable
fun contenidoRegistrar(
    listadoUsuario: List<Usuario>,
    socio: Socio?,
    onRegistrarClick: (String) -> Unit,
    onVolverClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope
) {

    /*
    * INICIALIZACION BD Y ESTADOS LOCALES
    * MANEJO DE FORMULARIO DE REGISTRO
    * */

    val context = LocalContext.current
    val dbHelper = remember { BibliotecaSQLite(context) }

    var nuevoUsuario by remember { mutableStateOf("") }
    var nuevaClave by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }
    var mensajeExito by remember { mutableStateOf("") }

    fun validarRegistro(): Boolean {

        /*
        * VALIDACION DE CAMPOS Y DUPLICIDAD DE USUARIO
        * */

        if (nuevoUsuario.isEmpty() || nuevaClave.isEmpty()) {
            mensajeError = "Complete todos los campos"
            return false
        }

        for (u in listadoUsuario) {
            if (u.usuario == nuevoUsuario) {
                mensajeError = "El usuario ya existe"
                return false
            }
        }

        return true
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        /*
        * FRAGMENTO DE LOGO Y ENCABEZADO PRINCIPAL
        * */

        LogoAplicacion(ContenidoBajoImagen = {
            Spacer(modifier = Modifier.height(15.dp))

            Text(
                text = "Formulario Registro",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
        })

        Spacer(modifier = Modifier.height(25.dp))

        /*
        * CAMPO ENTRADA NOMBRE DE USUARIO
        * */

        OutlinedTextField(
            value = nuevoUsuario,
            onValueChange = {
                nuevoUsuario = it
                mensajeError = ""
                mensajeExito = ""
            },
            label = { Text("Usuario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        /*
        * CAMPO ENTRADA CONTRASEÑA
        * */

        OutlinedTextField(
            value = nuevaClave,
            onValueChange = {
                nuevaClave = it
                mensajeError = ""
                mensajeExito = ""
            },
            label = { Text("Contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        /*
        * DESPLIEGUE DE MENSAJES DE ERROR O EXITO
        * */

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

        /*
        * BOTON DE CONFIRMACION Y PERSISTENCIA DE REGISTRO
        * */

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            onClick = {
                if (validarRegistro()) {

                    tono.startTone(ToneGenerator.TONE_PROP_ACK, 300)

                    val nuevaCuenta = Usuario(
                        usuario = nuevoUsuario,
                        clave = nuevaClave,
                        estadoCuenta = true,
                        socio = socio
                    )

                    /*
                    * PERSISTENCIA BD LOCAL EN SQLITE
                    * ALMACENAMIENTO PERMANENTE DEL USUARIO
                    * */

                    dbHelper.insertarUsuario(nuevaCuenta)

                    mensajeExito = "Usuario registrado con éxito"
                    mensajeError = ""

                    scope.launch {
                        snackbarHostState.showSnackbar("Usuario registrado con éxito")
                    }

                    onRegistrarClick(nuevoUsuario)

                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar(mensajeError)
                    }
                }
            }
        ) {
            Text(
                text = "Registrar",
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(15.dp))

        /*
        * BOTON NAVEGACION HACIA PANTALLA ANTERIOR
        * */

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            onClick = {
                tono.startTone(ToneGenerator.TONE_PROP_ACK, 300)
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

        Spacer(modifier = Modifier.height(15.dp))

        /*
        * ACCESO DIRECTO MODO TEXTO CLICKEABLE
        * */

        Text(
            text = "¿Ya tienes cuenta? Inicia sesión",
            fontSize = 13.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                scope.launch {
                    snackbarHostState.showSnackbar("Volviendo a la pantalla principal")
                }
                onVolverClick()
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pantallaRegistrar(
    listadoUsuario: List<Usuario>,
    socio: Socio?,
    onRegistrarClick: (String) -> Unit,
    onVolverClick: () -> Unit
) {

    /*
    * CONTENEDOR PRINCIPAL CON SCAFFOLD Y TEMA DE COLOR
    * ESTRUCTURA BASE DE LA PANTALLA DE REGISTRO
    * */

    val snackbarHostState = remember { SnackbarHostState() }
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
                    title = { Text("Biblioteca Online") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                contenidoRegistrar(
                    onRegistrarClick = onRegistrarClick,
                    onVolverClick = onVolverClick,
                    snackbarHostState = snackbarHostState,
                    listadoUsuario = listadoUsuario,
                    socio = socio,
                    scope = scope
                )
            }
        }
    }
}