package com.example.bibliotecaapp.ui.screens.com.example.bibliotecaapp.data

import com.example.bibliotecaapp.data.Usuario

class Socio (

    /*
    * IMPLEMENTACION DE CLASE SOCIO PARA ASOCIAR UN USUARIO A UNA SOLA PERSONA
    * */


    var rut:String="",
    var primer_nombre:String="",
    var segundo_nombre:String?=null,
    var apellido_paterno:String="",
    var apellido_materno:String="",
    var estado:Boolean?=null
    ){

}


public var listaSocios=arrayOf(
    Socio("18775116-0","Luciano","Romario",
        "Marin","Villanueva",true),
    Socio("9415449-9","Lujana","Eunice",
        "Villanueva","Yavar",true),
    Socio("9862879-7","Mario","Baltazar",
        "Marin","Reyes",true),
    Socio("19029771-3","Jocelyn","Carrasco",
        "Carrasco","Carrasco",true),
    Socio("1111111-1","Prueba","Prueba",
        "Prueba","Prueba",true),

    )