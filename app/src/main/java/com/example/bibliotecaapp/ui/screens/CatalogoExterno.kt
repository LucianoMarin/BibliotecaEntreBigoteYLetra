package com.example.bibliotecaapp.ui.screens

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.R
import com.example.bibliotecaapp.network.BookItem
import com.example.bibliotecaapp.network.RetrofitClient
import com.example.bibliotecaapp.ui.components.MenuLateral
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException

/*
* GENERADOR DE TONO DE NOTIFICACION
* USADO PARA EL SONIDO AL VOLVER
* */
private val tonoCatalogo = ToneGenerator(
    AudioManager.STREAM_NOTIFICATION,
    80
)

fun filtrarConTitulo(libros: List<BookItem>): List<BookItem> {

    /*
    * FILTRA LIBROS QUE TENGAN TITULO NO VACIO
    * */

    return libros.filter { it.volumeInfo.title.isNotBlank() }
}

fun tomarCincoAlAzar(libros: List<BookItem>): List<BookItem> {

    /*
    * SELECCIONA 5 LIBROS ALEATORIOS
    * */

    return libros.shuffled().take(5)
}

fun tomarPrimero(libros: List<BookItem>): List<BookItem> {

    /*
    * RETORNA SOLO EL PRIMER ELEMENTO DE LA LISTA
    * */

    if (libros.isEmpty()) {
        return emptyList()
    }
    return listOf(libros.first())
}

suspend fun buscarConReintento(
    busqueda: String,
    apiKey: String
): List<BookItem> {

    /*
    * REINTENTA LA BUSQUEDA EN API HASTA 3 VECES
    * SI OCURRE UN ERROR 503 DE SERVIDOR
    * */

    var intentos = 0
    val maxIntentos = 3
    var ultimaExcepcion: Exception? = null

    while (intentos < maxIntentos) {
        try {
            val respuesta = RetrofitClient.instance.obtenerLibros(
                busqueda = busqueda,
                apiKey = apiKey
            )
            return respuesta.items ?: emptyList()
        } catch (e: HttpException) {
            if (e.code() == 503) {
                ultimaExcepcion = e
                intentos = intentos + 1
                delay(1000L * intentos)
            } else {
                throw e
            }
        }
    }

    throw ultimaExcepcion ?: RuntimeException("Sin respuesta del servidor")
}

@Composable
fun tarjetaLibroExterno(libro: BookItem) {

    /*
    * COMPONENTE TARJETA PARA MOSTRAR
    * LA INFORMACION DE UN LIBRO EXTERNO
    * */

    val info = libro.volumeInfo

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "ID: " + libro.id,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Text(
                text = "Titulo: " + info.title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Autor: " + (info.authors?.joinToString(", ") ?: "Autor desconocido"),
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = info.description ?: "Sin descripción",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun contenidoCatalogoExterno(
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope
) {

    /*
    * VISTA DE CONTENIDO Y BUSQUEDA
    * MANEJA ESTADOS DE CAGA Y RESULTADOS
    * */

    var libros by remember {
        mutableStateOf<List<BookItem>>(emptyList())
    }

    var busqueda by remember {
        mutableStateOf("")
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    LaunchedEffect (Unit) {
        cargando = true
        try {
            val items = buscarConReintento(
                busqueda = "programacion",
                apiKey = "AIzaSyAtvk6oXe9pt6PdPRr01hgJmmSDiEHTHZI"
            )
            libros = tomarCincoAlAzar(filtrarConTitulo(items))
        } catch (e: Exception) {
            snackbarHostState.showSnackbar("Error al cargar libros: " + e.message)
        } finally {
            cargando = false
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))

        Image(
            painter = painterResource(id = R.drawable.libro),
            contentDescription = "Icono libro",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Catálogo Externo",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(15.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = { Text("Buscar libro por título") },
                modifier = Modifier.weight(1f),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                shape = RoundedCornerShape(4.dp),
                onClick = {
                    scope.launch {
                        cargando = true
                        val termino = busqueda.trim()
                        try {
                            val items = buscarConReintento(
                                busqueda = termino,
                                apiKey = "AIzaSyAtvk6oXe9pt6PdPRr01hgJmmSDiEHTHZI"
                            )
                            val filtrados = filtrarConTitulo(items)
                            if (filtrados.isEmpty()) {
                                snackbarHostState.showSnackbar("No se encontraron libros")
                            } else {
                                libros = tomarPrimero(filtrados)
                            }
                        } catch (e: HttpException) {
                            val codigo = e.code()
                            val cuerpo = e.response()?.errorBody()?.string() ?: "sin cuerpo"
                            android.util.Log.e("CATALOGO", "HTTP $codigo -> $cuerpo")
                            snackbarHostState.showSnackbar("HTTP $codigo: $cuerpo")
                        } catch (e: Exception) {
                            android.util.Log.e("CATALOGO", "Error: ${e.message}", e)
                            snackbarHostState.showSnackbar("Error: ${e.message}")
                        } finally {
                            cargando = false
                        }
                    }
                }
            ) {
                Text(text = "Buscar", fontSize = 15.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (cargando) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(15.dp))
        }

        Text(
            text = "Libros encontrados",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (libros.isEmpty() && !cargando) {
            Text(
                text = "No hay libros para mostrar",
                fontSize = 15.sp,
                color = Color.Red
            )
        } else {
            libros.forEach { libro ->
                tarjetaLibroExterno(libro = libro)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pantallaCatalogoExterno(
    onIrInicio: () -> Unit,
    onVolverClick: () -> Unit,
    onCerrarSesion: () -> Unit,
    onIrDisponibilidad: () -> Unit
) {

    /*
    * PANTALLA PRINCIPAL DE CATALOGO EXTERNO
    * CONTIENE ESTRUCTURA SCAFFOLD Y MENU DRAWER
    * */

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFBA9165),
            secondary = Color(0xFFFF9800),
            onPrimary = Color.White
        )
    ) {
        MenuLateral(
            drawerState = drawerState,
            scope = scope,
            onIrInicio = onIrInicio,
            onCerrarSesion = onCerrarSesion,
            contenidoExtraMenu = {
                NavigationDrawerItem(
                    label = { Text("Disponibilidad") },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        onIrDisponibilidad()
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Text("Biblioteca Online")
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            ) {
                                Text(
                                    text = "+",
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    fontSize = 24.sp
                                )
                            }
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
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    contenidoCatalogoExterno(
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        modifier = Modifier
                            .width(300.dp)
                            .height(50.dp)
                            .align(Alignment.CenterHorizontally),
                        shape = RoundedCornerShape(4.dp),
                        onClick = {
                            try {
                                tonoCatalogo.startTone(
                                    ToneGenerator.TONE_PROP_ACK,
                                    300
                                )
                            } catch (e: Exception) {
                                scope.launch {
                                    snackbarHostState.showSnackbar("No se pudo reproducir el sonido")
                                }
                            }
                            onVolverClick()
                        }
                    ) {
                        Text(
                            text = "Volver",
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}