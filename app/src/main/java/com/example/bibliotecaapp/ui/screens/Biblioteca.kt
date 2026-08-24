package com.example.bibliotecaapp.ui.screens

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.R
import com.example.bibliotecaapp.data.Libro
import com.example.bibliotecaapp.data.libros
import kotlinx.coroutines.launch

class Biblioteca {

    private val tono = ToneGenerator(
        AudioManager.STREAM_NOTIFICATION,
        80)
    @Composable
    fun cardPrincpipalBiblioteca(
        nombreUsuario: String,
        onCerrarSesionClick: () -> Unit
    ) {
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
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
                    text = "Bienvenido a BiblioRED",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.perfil),
                        contentDescription = "Logotipo de un gato leyendo",
                        modifier = Modifier.size(170.dp)
                    )

                    VerticalDivider(
                        modifier = Modifier
                            .height(120.dp)
                            .padding(horizontal = 16.dp)
                    )

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Usuario",
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )

                        Text(
                            text = nombreUsuario,
                            fontSize = 25.sp,
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    modifier = Modifier
                        .width(200.dp)
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally),
                    shape = RoundedCornerShape(4.dp),
                    onClick = {     tono.startTone(
                        ToneGenerator.TONE_PROP_ACK,
                        300
                    )
                        onCerrarSesionClick()
                    }
                ) {
                    Text(
                        text = "Cerrar Sesión",
                        fontSize = 20.sp
                    )
                }
            }
        }
    }


    @Composable
    fun opcionesBotones(){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                modifier = Modifier
                    .width(150.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(4.dp),
                onClick = {}
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.prestamo),
                        contentDescription = "Icono de libro",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Prestamos",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                modifier = Modifier
                    .width(150.dp)
                    .height(70.dp),
                shape = RoundedCornerShape(4.dp),
                onClick = {}
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.prestamo),
                        contentDescription = "Icono de libro",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "Devolucion",
                        fontSize = 16.sp
                    )
                }
            }

        }
    }


    @Composable
    fun listadoLibros(libros: Array<Libro>) {

        Row() {


            Image(
                painter = painterResource(id = R.drawable.libro),
                contentDescription = "Icono Libro",
                modifier = Modifier.size(40.dp),

                )

            Text(
                text = "Ultimos Libros",
                fontSize = 25.sp,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp),
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )
        }



        for (l in libros) {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
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
                        text = "Titulo: " + l.titulo,
                        fontSize = 17.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "Autor: " + l.autor,
                        fontSize = 17.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "Reseña: " + l.descripcion,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )
                    Text(
                        text = "Disponibilidad: " + l.estado,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun pantallaBiblioteca(
        nombreUsuario: String,
        onCerrarSesionClick: () -> Unit
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
                        .padding(24.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    cardPrincpipalBiblioteca(
                        nombreUsuario = nombreUsuario,
                        onCerrarSesionClick = onCerrarSesionClick
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    opcionesBotones()

                    Spacer(modifier = Modifier.height(35.dp))

                    listadoLibros(libros)

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
}