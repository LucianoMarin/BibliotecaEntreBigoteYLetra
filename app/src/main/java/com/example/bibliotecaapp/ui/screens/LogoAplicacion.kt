package com.example.bibliotecaapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.bibliotecaapp.R

@Composable
fun LogoAplicacion(ContenidoBajoImagen: @Composable () -> Unit = {}) {

    /*
    * COMPONENTE REUTILIZABLE DE LOGOTIPO
    * DESPLIEGA IMAGEN PRINCIPAL Y CONTENIDO ADICIONAL
    * */

    /*
    * CARGA Y DESPLIEGUE DEL LOGOTIPO DE LA APLICACION
    * */

    Image(
        painter = painterResource(id = R.drawable.logoprincipal),
        contentDescription = "Logotipo de un gato leyendo",
        modifier = Modifier.height(220.dp)
    )

    Spacer(
        modifier = Modifier.height(5.dp)
    )

    /*
    * SLOT PARA INYECTAR COMPOSABLES DEBAJO DE LA IMAGEN
    * */

    ContenidoBajoImagen()
}