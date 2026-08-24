package com.example.bibliotecaapp.data

class Libro(
    val codigo:String,
    val titulo:String,
    val descripcion:String,
    val autor:String,
    val estado:String
);

val libros=arrayOf(
    Libro("010101","Metamorfosis","arra la tragedia de Gregorio Samsa, quien despierta convertido en un insecto y sufre el cruel rechazo de su propia familia al dejar de ser el sostén económico del hogar",
        "Kafka","Disponble"),
    Libro("020202","Relatos Que miente un poco","Un recorrido profundo por su vida y el mito del rock nacional, donde la memoria se mezcla con la música y la leyenda.",
        "Indio Solari","Disponible")
    )

