package com.example.bibliotecaapp.data

class Libro(

    /*
    * CLASE LIBRO
    * CON IMPLEMENTACION DE INTERFAZ PRESTABLE
    * */

    var codigo:String="",
    var titulo:String="",
    var descripcion:String="",
    var autor:String="",
    var estado:String=""
):Prestable {

    override fun prestar(): String {
        estado = "Prestado"
        return "Libro $titulo prestado"
    }

    override fun devolver(): String {
        estado = "Disponible"
        return "Libro $titulo devuelto"
    }

    override fun estaDisponible(): Boolean {
        return estado == "Disponible"
    }


}




