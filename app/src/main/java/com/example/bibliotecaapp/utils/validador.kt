package com.example.bibliotecaapp.utils

import com.example.bibliotecaapp.data.Libro
import com.example.bibliotecaapp.data.Usuario

class Validator {

    fun validarLogin(
        listadoUsuario: List<Usuario>,
        usuario: String,
        clave: String
    ): Boolean {
        for (u in listadoUsuario) {
            if (u.usuario == usuario && u.clave == clave) {
                return true
            }
        }
        return false
    }

    fun validarRegistro(
        listadoUsuario: List<Usuario>,
        nuevoUsuario: String,
        nuevaClave: String
    ): String {
        if (nuevoUsuario.isEmpty() || nuevaClave.isEmpty()) {
            return "Complete todos los campos"
        }
        for (u in listadoUsuario) {
            if (u.usuario == nuevoUsuario) {
                return "El usuario ya existe"
            }
        }
        return ""
    }

    fun validarRut(rut: String): Boolean {
        if (rut.isEmpty()) {
            return false
        }
        if (rut.length < 8) {
            return false
        }
        return true
    }

    fun validarNombre(texto: String): Boolean {
        if (texto.isEmpty()) {
            return false
        }
        for (letra in texto) {
            if (!letra.isLetter() && letra != ' ') {
                return false
            }
        }
        return true
    }

    fun contarDisponibles(libros: List<Libro>): Int {
        var contador = 0
        for (l in libros) {
            if (l.estado == "Disponible") {
                contador = contador + 1
            }
        }
        return contador
    }

    fun contarPrestados(libros: List<Libro>): Int {
        var contador = 0
        for (l in libros) {
            if (l.estado == "Prestado") {
                contador = contador + 1
            }
        }
        return contador
    }

    fun filtrarDisponibles(libros: List<Libro>): List<Libro> {
        return libros.filter { it.estado == "Disponible" }
    }

    fun filtrarPrestados(libros: List<Libro>): List<Libro> {
        return libros.filter { it.estado == "Prestado" }
    }

    fun buscarPorTitulo(libros: List<Libro>, texto: String): List<Libro> {
        try {
            return libros.filter { it.titulo.contains(texto, ignoreCase = true) }
        } catch (e: Exception) {
            return emptyList()
        }
    }
}