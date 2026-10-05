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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.R
import com.example.bibliotecaapp.data.Libro
import com.example.bibliotecaapp.ui.components.MenuLateral
import kotlinx.coroutines.launch


/* variable privada del archivo, se usa para el sonido de los botones */
private val tono = ToneGenerator(
    AudioManager.STREAM_NOTIFICATION,
    80
)


/* SEMANA 5 - FUNCION DE ORDEN SUPERIOR + FILTER
   recibe la lista y devuelve solo los prestados */
fun filtrarPrestadosLista(libros: MutableList<Libro>): List<Libro> {
    return libros.filter { it.estado == "Prestado" }
}

fun filtrarDisponiblesLista(libros: MutableList<Libro>): List<Libro> {
    return libros.filter { it.estado == "Disponible" }
}

/* cuenta prestados con for, sin usar filter */
fun contarPrestadosLista(libros: MutableList<Libro>): Int {
    var contador = 0
    for (l in libros) {
        if (l.estado == "Prestado") {
            contador = contador + 1
        }
    }
    return contador
}

fun contarDisponiblesLista(libros: MutableList<Libro>): Int {
    var contador = 0
    for (l in libros) {
        if (l.estado == "Disponible") {
            contador = contador + 1
        }
    }
    return contador
}


@Composable
fun tarjetaPrestamo(
    libro: Libro,
    onDevolver: (Libro) -> Unit,
    onPrestar: (Libro) -> Unit
) {

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
                text = "Codigo: " + libro.codigo,
                fontSize = 15.sp,
                color = Color.Black
            )
            Text(
                text = "Titulo: " + libro.titulo,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Autor: " + libro.autor,
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))

            /* si el libro esta disponible muestro boton Prestar, si no, Devolver */
            if (libro.estaDisponible()) {
                Text(
                    text = "Estado: Disponible",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        onPrestar(libro)
                    }
                ) {
                    Text(
                        text = "Prestar",
                        fontSize = 16.sp
                    )
                }
            } else {
                Text(
                    text = "Estado: Prestado",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        onDevolver(libro)
                    }
                ) {
                    Text(
                        text = "Devolver",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}


@Composable
fun contenidoPrestamos(
    listadoLibros: MutableList<Libro>,
    onIrInicio: () -> Unit,
    onVolverClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope
) {

    /* contador que cambia cada vez que presto o devuelvo, sirve para redibujar */
    var recargar by remember {
        mutableStateOf(0)
    }

    val prestados = remember(recargar) {
        filtrarPrestadosLista(listadoLibros)
    }

    val disponibles = remember(recargar) {
        filtrarDisponiblesLista(listadoLibros)
    }

    val totalPrestados = remember(recargar) {
        contarPrestadosLista(listadoLibros)
    }

    val totalDisponibles = remember(recargar) {
        contarDisponiblesLista(listadoLibros)
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
            text = "Prestamos y Devoluciones",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(15.dp))

        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Resumen",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Disponibles: " + totalDisponibles,
                    fontSize = 15.sp,
                    color = Color(0xFF2E7D32)
                )
                Text(
                    text = "Prestados: " + totalPrestados,
                    fontSize = 15.sp,
                    color = Color.Red
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Libros prestados",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        /* si no hay prestados muestro un aviso, si hay los dibujo */
        if (prestados.isEmpty()) {
            Text(
                text = "No hay libros prestados",
                fontSize = 15.sp,
                color = Color.Red
            )
        } else {
            prestados.forEach { libro ->
                tarjetaPrestamo(
                    libro = libro,
                    onDevolver = { l ->
                        /* SEMANA 5 - TRY/CATCH
                           si el tono falla, aviso por snackbar */
                        try {
                            tono.startTone(
                                ToneGenerator.TONE_PROP_ACK,
                                300
                            )
                        } catch (e: Exception) {
                            scope.launch {
                                snackbarHostState.showSnackbar("No se pudo reproducir el sonido")
                            }
                        }
                        /* SEMANA 4 - METODO DE LA CLASE LIBRO
                           devolver() cambia el estado del libro */
                        val mensaje = l.devolver()
                        recargar = recargar + 1
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje)
                        }
                    },
                    onPrestar = { l ->
                        try {
                            tono.startTone(
                                ToneGenerator.TONE_PROP_ACK,
                                300
                            )
                        } catch (e: Exception) {
                            scope.launch {
                                snackbarHostState.showSnackbar("No se pudo reproducir el sonido")
                            }
                        }
                        val mensaje = l.prestar()
                        recargar = recargar + 1
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Libros disponibles",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (disponibles.isEmpty()) {
            Text(
                text = "No hay libros disponibles",
                fontSize = 15.sp,
                color = Color.Red
            )
        } else {
            disponibles.forEach { libro ->
                tarjetaPrestamo(
                    libro = libro,
                    onDevolver = { l ->
                        try {
                            tono.startTone(
                                ToneGenerator.TONE_PROP_ACK,
                                300
                            )
                        } catch (e: Exception) {
                            scope.launch {
                                snackbarHostState.showSnackbar("No se pudo reproducir el sonido")
                            }
                        }
                        val mensaje = l.devolver()
                        recargar = recargar + 1
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje)
                        }
                    },
                    onPrestar = { l ->
                        try {
                            tono.startTone(
                                ToneGenerator.TONE_PROP_ACK,
                                300
                            )
                        } catch (e: Exception) {
                            scope.launch {
                                snackbarHostState.showSnackbar("No se pudo reproducir el sonido")
                            }
                        }
                        val mensaje = l.prestar()
                        recargar = recargar + 1
                        scope.launch {
                            snackbarHostState.showSnackbar(mensaje)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                try {
                    tono.startTone(
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


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pantallaPrestamos(
    listadoLibros: MutableList<Libro>,
    onIrInicio: () -> Unit,
    onVolverClick: () -> Unit,
    onCerrarSesion:()->Unit,
    onIrDisponibilidad:()->Unit
) {

    /* estado del snackbar, sirve para mostrar mensajes abajo */
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    /* estado del menu lateral, empieza cerrado */
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


                // Opción: Disponibilidad
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
                            /* boton hamburguesa, abre el menu */
                            IconButton(
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            ) {
                                Text(
                                    text = "☰",
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
                    contenidoPrestamos(
                        listadoLibros = listadoLibros,
                        onIrInicio = onIrInicio,
                        onVolverClick = onVolverClick,
                        snackbarHostState = snackbarHostState,
                        scope = scope
                    )
                }
            }
        }
    }
}