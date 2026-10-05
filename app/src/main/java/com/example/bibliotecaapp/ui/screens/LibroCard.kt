package com.example.bibliotecaapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bibliotecaapp.data.Libro

@Composable
fun LibroCard(
    libro: Libro,
    contenidoExtra: @Composable ()->Unit={}){

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {


            contenidoExtra()


            Text(
                text = "Titulo: " + libro.titulo,
                fontSize = 17.sp,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Text(
                text = "Autor: " + libro.autor,
                fontSize = 17.sp,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )
            Text(
                text = "Reseña: " + libro.descripcion,
                fontSize = 16.sp,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.Black
            )

        }
    }




    Spacer(modifier = Modifier.height(16.dp))

}



