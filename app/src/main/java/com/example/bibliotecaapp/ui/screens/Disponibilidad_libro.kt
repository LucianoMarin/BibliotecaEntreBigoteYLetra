package com.example.bibliotecaapp.ui.screens

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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


/* SEMANA 5 - PROPIEDAD DE EXTENSION
   agrega la propiedad "disponible" a la clase Libro sin tocarla */
val Libro.disponible: Boolean
    get() = estado == "Disponible"

val Libro.prestado: Boolean
    get() = estado == "Prestado"


/* SEMANA 5 - FUNCION DE EXTENSION
   agrega el metodo estaDisponible() a la clase Libro */
fun Libro.estaDisponible(): Boolean {
    return this.estado == "Disponible"
}

fun Libro.estaPrestado(): Boolean {
    return this.estado == "Prestado"
}

/* SEMANA 5 - FUNCION DE EXTENSION SOBRE LA LISTA
   cuenta cuantos disponibles hay usando un for */
fun MutableList<Libro>.totalDisponibles(): Int {
    var contador = 0
    for (l in this) {
        if (l.estaDisponible()) {
            contador = contador + 1
        }
    }
    return contador
}

fun MutableList<Libro>.totalPrestados(): Int {
    var contador = 0
    for (l in this) {
        if (l.estaPrestado()) {
            contador = contador + 1
        }
    }
    return contador
}


fun esDisponible(libro: Libro): Boolean {
    return libro.estaDisponible()
}

fun esPrestado(libro: Libro): Boolean {
    return libro.estaPrestado()
}

/* SEMANA 5 - FUNCION DE ORDEN SUPERIOR + FILTER
   recibe una lista y devuelve solo los disponibles */
fun filtrarDisponibles(libros: MutableList<Libro>): List<Libro> {
    return libros.filter { esDisponible(it) }
}

fun filtrarPrestados(libros: MutableList<Libro>): List<Libro> {
    return libros.filter { esPrestado(it) }
}

/* cuenta disponibles con for, sin usar filter */
fun contarDisponibles(libros: MutableList<Libro>): Int {
    var contador = 0
    for (l in libros) {
        if (esDisponible(l)) {
            contador = contador + 1
        }
    }
    return contador
}

fun contarPrestados(libros: MutableList<Libro>): Int {
    var contador = 0
    for (l in libros) {
        if (esPrestado(l)) {
            contador = contador + 1
        }
    }
    return contador
}

/* SEMANA 5 - TRY/CATCH
   busca libros por titulo, si algo falla devuelve lista vacia */
fun buscarPorTitulo(libros: MutableList<Libro>, texto: String): List<Libro> {
    try {
        return libros.filter { it.titulo.contains(texto, ignoreCase = true) }
    } catch (e: Exception) {
        return emptyList()
    }
}


@Composable
fun filtrosDisponibilidad(
    filtro: String,
    onCambiarFiltro: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .width(110.dp)
                .fillMaxHeight(),
            shape = RoundedCornerShape(2.dp),
            contentPadding = PaddingValues(
                start = 4.dp,
                end = 4.dp,
                top = 0.dp,
                bottom = 0.dp
            ),
            onClick = {
                onCambiarFiltro("Todos")
            }
        ) {
            Text(
                text = "Todos",
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Button(
            modifier = Modifier
                .width(110.dp)
                .fillMaxHeight(),
            shape = RoundedCornerShape(2.dp),
            contentPadding = PaddingValues(
                start = 4.dp,
                end = 4.dp,
                top = 0.dp,
                bottom = 0.dp
            ),
            onClick = {
                onCambiarFiltro("Disponible")
            }
        ) {
            Text(
                text = "Disponibles",
                fontSize = 12.sp,
                maxLines = 1
            )
        }

        Spacer(modifier = Modifier.width(2.dp))

        Button(
            modifier = Modifier
                .width(110.dp)
                .fillMaxHeight(),
            shape = RoundedCornerShape(2.dp),
            contentPadding = PaddingValues(
                start = 4.dp,
                end = 4.dp,
                top = 0.dp,
                bottom = 0.dp
            ),
            onClick = {
                onCambiarFiltro("Prestado")
            }
        ) {
            Text(
                text = "Prestados",
                fontSize = 12.sp,
                maxLines = 1
            )
        }
    }
}


@Composable
fun contenidoDisponibilidad(
    listadoLibros: MutableList<Libro>,
    onIrInicio: () -> Unit,
    onIrPrestamos: () -> Unit,
    onVolverClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
    scope: kotlinx.coroutines.CoroutineScope
) {

    /* guarda el filtro actual, empieza en Todos */
    var filtro by remember {
        mutableStateOf("Todos")
    }

    /* segun el filtro, el when elige que lista mostrar */
    val librosFiltrados = when (filtro) {
        "Disponible" -> filtrarDisponibles(listadoLibros)
        "Prestado" -> filtrarPrestados(listadoLibros)
        else -> listadoLibros
    }

    val totalLibros = listadoLibros.size
    val totalDisponibles = listadoLibros.totalDisponibles()
    val totalPrestados = listadoLibros.totalPrestados()

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
            text = "Disponibilidad de Libros",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            style = MaterialTheme.typography.headlineMedium,
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
                    text = "Resumen del catalogo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Total de libros: " + totalLibros,
                    fontSize = 15.sp,
                    color = Color.Black
                )
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
            text = "Filtrar por estado",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(10.dp))

        filtrosDisponibilidad(
            filtro = filtro,
            onCambiarFiltro = { nuevo ->
                filtro = nuevo
                scope.launch {
                    snackbarHostState.showSnackbar("Mostrando: $nuevo")
                }
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        /* si no hay libros filtrados muestro un aviso, si hay los dibujo */
        if (librosFiltrados.isEmpty()) {
            Text(
                text = "No hay libros en esta categoria",
                fontSize = 16.sp,
                color = Color.Red
            )
        } else {

            librosFiltrados.forEach { libros ->

                /* FRAGMENT DE CARD DE LIBROS  VISTO EN LA SESION NUMERO 6 */

                LibroCard(libros, contenidoExtra = {

                    Text(
                        text = "codigo: " + libros.codigo,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )

                    if (libros.disponible) {
                        Text(
                            text = "SE ENCUENTRA: " + libros.estado,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32)
                        )
                    } else {
                        Text(
                            text = "SE ENCUENTRA: " + libros.estado,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }


                })
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            modifier = Modifier
                .width(300.dp)
                .height(50.dp),
            shape = RoundedCornerShape(4.dp),
            onClick = {
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
fun pantallaDisponibilidad(
    listadoLibros: MutableList<Libro>,
    onIrInicio: () -> Unit,
    onIrPrestamos: () -> Unit,
    onVolverClick: () -> Unit
) {

    /* estado del snackbar, sirve para mostrar mensajes abajo */
    val snackbarHostState = remember {
        SnackbarHostState()
    }

    /* estado del menu lateral, empieza cerrado */
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    /* scope para abrir y cerrar el menu */
    val scope = rememberCoroutineScope()

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFBA9165),
            secondary = Color(0xFFFF9800),
            onPrimary = Color.White
        )
    ) {

        /*
         * MENU LATERAL
         *
         * Las opciones Inicio y Cerrar sesión
         * son comunes a todas las pantallas.
         *
         * Préstamos es una opción adicional
         * específica de esta pantalla.
         */
        MenuLateral(
            drawerState = drawerState,
            scope = scope,

            // Opción Inicio
            onIrInicio = onIrInicio,

            // Opción Cerrar sesión
            onCerrarSesion = onIrInicio,

            // Opciones adicionales de esta pantalla
            contenidoExtraMenu = {

                NavigationDrawerItem(
                    label = {
                        Text("Préstamos")
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }

                        onIrPrestamos()
                    }
                )
            }

        ) {

            /*
             * TODO EL SCAFFOLD ES EL CONTENIDO
             * QUE ESTÁ DENTRO DEL MENU LATERAL
             */
            Scaffold(

                topBar = {

                    TopAppBar(

                        title = {
                            Text("Biblioteca Online")
                        },

                        navigationIcon = {

                            /*
                             * Botón hamburguesa
                             * abre el menú lateral
                             */
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        drawerState.open()
                                    }
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
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {

                    contenidoDisponibilidad(
                        listadoLibros = listadoLibros,

                        onIrInicio = onIrInicio,

                        onIrPrestamos = onIrPrestamos,

                        onVolverClick = onVolverClick,

                        snackbarHostState = snackbarHostState,

                        scope = scope
                    )
                }
            }
        }
    }




}