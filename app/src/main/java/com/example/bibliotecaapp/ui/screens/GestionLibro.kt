package com.example.bibliotecaapp.ui.screens

import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.data.Libro
import com.example.bibliotecaapp.data.sqlite.BibliotecaSQLite
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.ExperimentalMaterial3Api

/*
* CONFIGURACION DE SONIDO CRUD
* EMISION DE TONO PARA ACCIONES DE GESTION
* */

private val tonoCrud = ToneGenerator(
    AudioManager.STREAM_NOTIFICATION,
    80
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun pantallaGestionLibros(
    onVolver: () -> Unit
) {

    /*
    * INICIALIZACION DE BD Y ESTADOS PANTALLA
    * MANEJO DE LISTADO DE LIBROS Y DIALOGOS
    * */

    val context = androidx.compose.ui.platform.LocalContext.current

    val bibliotecaSQLite = remember {
        BibliotecaSQLite(context)
    }

    var libros by remember {
        mutableStateOf(bibliotecaSQLite.listarLibros())
    }

    var mostrarFormulario by remember {
        mutableStateOf(false)
    }

    var libroEditar by remember {
        mutableStateOf<Libro?>(null)
    }

    var libroEliminar by remember {
        mutableStateOf<Libro?>(null)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    /*
    * RECARGA DE LISTA DE LIBROS DESDE SQLITE
    * */

    fun actualizarLista() {
        libros = bibliotecaSQLite.listarLibros()
    }

    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Color(0xFFBA9165),
            secondary = Color(0xFFFF9800),
            onPrimary = Color.White
        )
    ) {

        /*
        * ESTRUCTURA PRINCIPAL DE PANTALLA CON BARRA SUPERIOR
        * */

        Scaffold(

            topBar = {
                TopAppBar(
                    title = {
                        Text("Gestión de Libros")
                    },
                    navigationIcon = {
                        TextButton(
                            onClick = {
                                tonoCrud.startTone(
                                    ToneGenerator.TONE_PROP_ACK,
                                    200
                                )
                                onVolver()
                            }
                        ) {
                            Text(
                                text = "Volver",
                                color = MaterialTheme.colorScheme.onPrimary
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

            /*
            * CONTENIDO SCROLLABLE DE ADMINISTRACION
            * */

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {

                Text(
                    text = "Administración de libros",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Desde aquí puedes agregar, modificar o eliminar libros.",
                    fontSize = 15.sp,
                    color = Color.DarkGray
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                /*
                * BOTON APERTURA FORMULARIO NUEVO LIBRO
                * */

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {

                        tonoCrud.startTone(
                            ToneGenerator.TONE_PROP_ACK,
                            200
                        )

                        libroEditar = null
                        mostrarFormulario = true
                    }
                ) {
                    Text(
                        text = "Nuevo Libro",
                        fontSize = 18.sp
                    )
                }

                Spacer(
                    modifier = Modifier.height(25.dp)
                )

                Text(
                    text = "Libros registrados",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                /*
                * VALIDACION DE LISTA VACIA O DESPLIEGUE DE TARJETAS
                * */

                if (libros.isEmpty()) {

                    Text(
                        text = "No existen libros registrados.",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )

                } else {

                    libros.forEach { libro ->

                        LibroGestionCard(
                            libro = libro,

                            onEditar = {
                                tonoCrud.startTone(
                                    ToneGenerator.TONE_PROP_ACK,
                                    200
                                )

                                libroEditar = libro
                                mostrarFormulario = true
                            },

                            onEliminar = {
                                tonoCrud.startTone(
                                    ToneGenerator.TONE_PROP_ACK,
                                    200
                                )

                                libroEliminar = libro
                            },

                            onCambiarEstado = {

                                /*
                                * CAMBIO DE ESTADO EN BASE DE DATOS
                                * PRESTAMO O DEVOLUCION DE LIBRO
                                * */

                                val resultado: Boolean

                                if (libro.estado == "Disponible") {

                                    resultado = bibliotecaSQLite.prestarLibro(
                                        libro.codigo
                                    )

                                } else {

                                    resultado = bibliotecaSQLite.devolverLibro(
                                        libro.codigo
                                    )
                                }

                                if (resultado) {

                                    actualizarLista()

                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            if (libro.estado == "Disponible") {
                                                "Libro marcado como prestado"
                                            } else {
                                                "Libro marcado como disponible"
                                            }
                                        )
                                    }
                                }
                            }
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )
                    }
                }
            }
        }

        /*
        * DIALOGO FLOTANTE DE FORMULARIO CREAR/EDITAR
        * */

        if (mostrarFormulario) {

            FormularioLibro(
                libro = libroEditar,

                onCerrar = {
                    mostrarFormulario = false
                    libroEditar = null
                },

                onGuardar = { libro ->

                    /*
                    * PERSISTENCIA BD: INSERTAR O ACTUALIZAR
                    * */

                    try {

                        if (libroEditar == null) {

                            bibliotecaSQLite.insertarLibro(libro)

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Libro agregado correctamente"
                                )
                            }

                        } else {

                            bibliotecaSQLite.actualizarLibro(libro)

                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Libro actualizado correctamente"
                                )
                            }
                        }

                        actualizarLista()

                        mostrarFormulario = false
                        libroEditar = null

                    } catch (e: Exception) {

                        scope.launch {
                            snackbarHostState.showSnackbar(
                                "No se pudo guardar el libro"
                            )
                        }
                    }
                }
            )
        }

        /*
        * DIALOGO DE CONFIRMACION PARA ELIMINAR LIBRO
        * */

        if (libroEliminar != null) {

            AlertDialog(
                onDismissRequest = {
                    libroEliminar = null
                },

                title = {
                    Text(
                        text = "Eliminar libro"
                    )
                },

                text = {
                    Text(
                        text = "¿Está seguro de eliminar el libro \"${libroEliminar!!.titulo}\"?"
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            /*
                            * ELIMINACION PERMANENTE EN SQLITE
                            * */

                            val eliminado =
                                bibliotecaSQLite.eliminarLibro(
                                    libroEliminar!!.codigo
                                )

                            if (eliminado) {

                                actualizarLista()

                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Libro eliminado correctamente"
                                    )
                                }
                            }

                            libroEliminar = null
                        }
                    ) {
                        Text(
                            text = "Eliminar",
                            color = Color.Red
                        )
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            libroEliminar = null
                        }
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun LibroGestionCard(
    libro: Libro,
    onEditar: () -> Unit,
    onEliminar: () -> Unit,
    onCambiarEstado: () -> Unit
) {

    /*
    * TARJETA INDIVIDUAL DE GESTION DE LIBRO
    * MUESTRA INFORMACION Y BOTONES DE ACCION
    * */

    Card(
        modifier = Modifier.fillMaxWidth(),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),

        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = libro.titulo,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Código: ${libro.codigo}",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Autor: ${libro.autor}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = libro.descripcion,
                fontSize = 15.sp,
                color = Color.DarkGray
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Divider()

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Estado: ${libro.estado}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (libro.estado == "Disponible") {
                    Color(0xFF2E7D32)
                } else {
                    Color(0xFFC62828)
                }
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            /*
            * BOTONES DE ACCIONES: EDITAR, PRESTAR/DEVOLVER Y ELIMINAR
            * */

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    modifier = Modifier.width(100.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        onEditar()
                    }
                ) {
                    Text(
                        text = "Editar",
                        fontSize = 13.sp
                    )
                }

                Button(
                    modifier = Modifier.width(110.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        onCambiarEstado()
                    }
                ) {
                    Text(
                        text = if (libro.estado == "Disponible") {
                            "Prestar"
                        } else {
                            "Devolver"
                        },
                        fontSize = 13.sp
                    )
                }

                Button(
                    modifier = Modifier.width(100.dp),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {
                        onEliminar()
                    }
                ) {
                    Text(
                        text = "Eliminar",
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}


@Composable
fun FormularioLibro(
    libro: Libro?,
    onCerrar: () -> Unit,
    onGuardar: (Libro) -> Unit
) {

    /*
    * FORMULARIO EN DIALOGO DE ENTRADA DE DATOS
    * SIRVE TANTO PARA CREAR COMO PARA EDITAR
    * */

    var codigo by remember {
        mutableStateOf(libro?.codigo ?: "")
    }

    var titulo by remember {
        mutableStateOf(libro?.titulo ?: "")
    }

    var descripcion by remember {
        mutableStateOf(libro?.descripcion ?: "")
    }

    var autor by remember {
        mutableStateOf(libro?.autor ?: "")
    }

    var estado by remember {
        mutableStateOf(libro?.estado ?: "Disponible")
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    AlertDialog(
        onDismissRequest = {
            onCerrar()
        },

        title = {
            Text(
                text = if (libro == null) {
                    "Nuevo Libro"
                } else {
                    "Editar Libro"
                }
            )
        },

        text = {

            Column {

                /*
                * CAMPOS DE TEXTO PARA DATOS DEL LIBRO
                * */

                OutlinedTextField(
                    value = codigo,
                    onValueChange = {
                        codigo = it
                    },
                    label = {
                        Text("Código")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = libro == null,
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                    },
                    label = {
                        Text("Título")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = autor,
                    onValueChange = {
                        autor = it
                    },
                    label = {
                        Text("Autor")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = {
                        descripcion = it
                    },
                    label = {
                        Text("Descripción")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                /*
                * SELECCION CONMUTABLE DE ESTADO
                * */

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = "Estado:"
                    )

                    Button(
                        onClick = {
                            estado =
                                if (estado == "Disponible") {
                                    "Prestado"
                                } else {
                                    "Disponible"
                                }
                        }
                    ) {
                        Text(
                            text = estado
                        )
                    }
                }

                if (mensajeError.isNotEmpty()) {

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text = mensajeError,
                        color = Color.Red,
                        fontSize = 14.sp
                    )
                }
            }
        },

        confirmButton = {

            TextButton(
                onClick = {

                    /*
                    * VALIDACION Y ENVIO DE DATOS
                    * */

                    if (
                        codigo.isBlank() ||
                        titulo.isBlank() ||
                        descripcion.isBlank() ||
                        autor.isBlank()
                    ) {

                        mensajeError =
                            "Complete todos los campos"

                    } else {

                        onGuardar(
                            Libro(
                                codigo = codigo.trim(),
                                titulo = titulo.trim(),
                                descripcion = descripcion.trim(),
                                autor = autor.trim(),
                                estado = estado
                            )
                        )
                    }
                }
            ) {
                Text("Guardar")
            }
        },

        dismissButton = {

            TextButton(
                onClick = {
                    onCerrar()
                }
            ) {
                Text("Cancelar")
            }
        }
    )
}