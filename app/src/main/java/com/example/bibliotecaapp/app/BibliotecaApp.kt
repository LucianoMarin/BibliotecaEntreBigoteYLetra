package com.example.bibliotecaapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.bibliotecaapp.data.SesionManager
import com.example.bibliotecaapp.data.sqlite.BibliotecaSQLite
import com.example.bibliotecaapp.ui.screens.com.example.bibliotecaapp.data.Socio

@Composable
fun BibliotecaApp() {

    val context = LocalContext.current

    val bibliotecaSQLite = remember {
        BibliotecaSQLite(context)
    }

    val sesionManager = remember {
        SesionManager(context)
    }

    var pantalla by remember {
        mutableStateOf(
            if (sesionManager.sesionActiva()) {
                "biblioteca"
            } else {
                "principal"
            }
        )
    }

    var nombreUsuario by remember {
        mutableStateOf(
            sesionManager.obtenerUsuario()
        )
    }

    var socioNuevo by remember {
        mutableStateOf<Socio?>(null)
    }

    when (pantalla) {

        "principal" -> pantallaInicio(

            onLoginClick = {
                pantalla = "login"
            },

            onRegistrarClick = {
                pantalla = "registrar"
            },

            onSkipLoginClick = {
                pantalla = "biblioteca"
            }
        )

        "login" -> pantallaLogin(

            listadoUsuario = bibliotecaSQLite.listarUsuarios(),

            onIngresarClick = { usuario, clave ->

                val usuarioBD = bibliotecaSQLite.obtenerUsuario(
                    usuario = usuario,
                    clave = clave
                )

                if (usuarioBD != null && usuarioBD.socio != null) {

                    sesionManager.iniciarSesion(
                        usuario = usuarioBD.usuario,
                        nombre = usuarioBD.socio!!.primer_nombre,
                        apellido = usuarioBD.socio!!.apellido_paterno
                    )

                    nombreUsuario = usuarioBD.usuario

                    pantalla = "biblioteca"
                }
            },

            onVolverClick = {
                pantalla = "principal"
            }
        )

        "registrar" -> pantallaDatosPersonales(

            bibliotecaSQLite = bibliotecaSQLite,

            onContinuarClick = {
                    rut,
                    pNombre,
                    sNombre,
                    aPaterno,
                    aMaterno ->

                socioNuevo = Socio(
                    rut = rut,
                    primer_nombre = pNombre,
                    segundo_nombre = if (sNombre.isEmpty()) null else sNombre,
                    apellido_paterno = aPaterno,
                    apellido_materno = aMaterno,
                    estado = true
                )

                pantalla = "registrarUsuario"
            },

            onVolverClick = {
                pantalla = "principal"
            }
        )

        "registrarUsuario" -> pantallaRegistrar(

            listadoUsuario = bibliotecaSQLite.listarUsuarios(),

            socio = socioNuevo,

            onRegistrarClick = { nombre ->
                nombreUsuario = nombre
                pantalla = "biblioteca"
            },

            onVolverClick = {
                pantalla = "principal"
            }
        )

        "biblioteca" -> pantallaBiblioteca(

            listadoUsuario = bibliotecaSQLite.listarUsuarios(),

            nombreUsuario = nombreUsuario,

            listadoLibros = bibliotecaSQLite.listarLibros(),

            onCerrarSesionClick = {
                sesionManager.cerrarSesion()
                nombreUsuario = ""
                pantalla = "principal"
            },

            onIrInicio = {
                pantalla = "biblioteca"
            },

            onIrPrestamos = {
                pantalla = "prestamos"
            },

            onIrDisponibilidad = {
                pantalla = "disponibilidad"
            },

            onCatalogoOnline = {
                pantalla = "catalocoexterno"
            },

            onIrGestionLibros = {
                pantalla = "gestionLibros"
            }
        )

        "disponibilidad" -> pantallaDisponibilidad(

            listadoLibros = bibliotecaSQLite.listarLibros(),

            onIrInicio = {
                pantalla = "biblioteca"
            },

            onIrPrestamos = {
                pantalla = "prestamos"
            },

            onVolverClick = {
                pantalla = "biblioteca"
            }
        )

        "prestamos" -> pantallaPrestamos(

            listadoLibros = bibliotecaSQLite.listarLibros(),

            onIrInicio = {
                pantalla = "biblioteca"
            },

            onCerrarSesion = {
                sesionManager.cerrarSesion()
                nombreUsuario = ""
                pantalla = "principal"
            },

            onIrDisponibilidad = {
                pantalla = "disponibilidad"
            },

            onVolverClick = {
                pantalla = "biblioteca"
            }
        )

        "catalocoexterno" -> pantallaCatalogoExterno(

            onIrInicio = {
                pantalla = "biblioteca"
            },

            onVolverClick = {
                pantalla = "biblioteca"
            },

            onCerrarSesion = {
                sesionManager.cerrarSesion()
                nombreUsuario = ""
                pantalla = "principal"
            },

            onIrDisponibilidad = {
                pantalla = "disponibilidad"
            }
        )

        "gestionLibros" -> pantallaGestionLibros(

            onVolver = {
                pantalla = "biblioteca"
            }
        )
    }
}