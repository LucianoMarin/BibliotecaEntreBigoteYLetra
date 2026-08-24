package com.example.bibliotecaapp.data

class Usuario (
    val usuario: String,
    val clave: String,
    val nombre: String
)

val usuarios = arrayOf(
    Usuario("lmarin", "123", "Luciano"),
    Usuario("lvillanueva", "123", "Lujana"),
    Usuario("csierra", "123", "Carla"),
    Usuario("mmarin", "123", "Mario"),
    Usuario("lperez", "858585", "Lucia")
)