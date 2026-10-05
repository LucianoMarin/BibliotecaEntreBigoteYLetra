package com.example.bibliotecaapp.data

import android.content.Context

class SesionManager(context: Context) {

    private val preferencias = context.getSharedPreferences(
        "sesion",
        Context.MODE_PRIVATE
    )

    fun iniciarSesion(
        usuario: String,
        nombre: String,
        apellido: String
    ) {
        preferencias.edit()
            .putBoolean("sesion_activa", true)
            .putString("usuario", usuario)
            .putString("nombre", nombre)
            .putString("apellido", apellido)
            .apply()
    }

    fun cerrarSesion() {
        preferencias.edit()
            .clear()
            .apply()
    }

    fun sesionActiva(): Boolean {
        return preferencias.getBoolean(
            "sesion_activa",
            false
        )
    }

    fun obtenerUsuario(): String {
        return preferencias.getString(
            "usuario",
            ""
        ) ?: ""
    }

    fun obtenerNombre(): String {
        return preferencias.getString(
            "nombre",
            ""
        ) ?: ""
    }

    fun obtenerApellido(): String {
        return preferencias.getString(
            "apellido",
            ""
        ) ?: ""
    }
}