package com.example.bibliotecaapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class BibliotecaApp {

    @Composable
    fun App() {
        var pantalla by remember {
            mutableStateOf("principal")
        }

        var nombreUsuario by remember {
            mutableStateOf("")
        }

        when (pantalla) {
            "principal" -> {
                val principal = Principal()
                principal.pantallaInicio(
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
            }
            "login" -> {
                val login = Login()
                login.pantallaLogin(
                    onIngresarClick = { nombre ->
                        nombreUsuario = nombre
                        pantalla = "biblioteca"
                    },
                    onVolverClick = {
                        pantalla = "principal"
                    }
                )
            }
            "registrar" -> {
                val registrar = Registrar()
                registrar.pantallaRegistrar(
                    onRegistrarClick = { nombre ->
                        nombreUsuario = nombre
                        pantalla = "biblioteca"
                    },
                    onVolverClick = {
                        pantalla = "principal"
                    }
                )
            }
            "biblioteca" -> {
                val biblioteca = Biblioteca()
                biblioteca.pantallaBiblioteca(
                    nombreUsuario = nombreUsuario,
                    onCerrarSesionClick = {
                        nombreUsuario = ""
                        pantalla = "principal"
                    }
                )
            }
        }
    }
}