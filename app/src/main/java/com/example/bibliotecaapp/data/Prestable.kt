package com.example.bibliotecaapp.data

interface Prestable {
    fun prestar(): String
    fun devolver(): String
    fun estaDisponible(): Boolean
}