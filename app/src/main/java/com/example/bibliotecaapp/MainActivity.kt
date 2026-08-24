package com.example.bibliotecaapp

import com.example.bibliotecaapp.ui.screens.Principal
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bibliotecaapp.ui.screens.Biblioteca
import com.example.bibliotecaapp.ui.screens.BibliotecaApp
import com.example.bibliotecaapp.ui.screens.Login
import com.example.bibliotecaapp.ui.theme.theme.BibliotecaAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme{
                        val app= BibliotecaApp();
                        app.App();
                    }
                }
            }
        }


