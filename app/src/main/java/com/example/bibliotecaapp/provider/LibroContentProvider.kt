package com.example.bibliotecaapp.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.bibliotecaapp.network.BookItem

class LibroContentProvider : ContentProvider() {

    companion object {
        const val AUTHORITY = "com.example.bibliotecaapp.libros"
        val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/libros")

        // Lista estática en memoria para almacenar la respuesta de la API
        private val libros = mutableListOf<BookItem>()

        // Carga los libros recibidos desde Retrofit
        fun cargarLibros(nuevosLibros: List<BookItem>) {
            libros.clear()
            libros.addAll(nuevosLibros)
        }
    }

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {

        // Define la estructura de las columnas de tu Cursor
        val cursor = MatrixCursor(
            arrayOf(
                "id",
                "titulo",
                "autores",
                "descripcion",
                "imagen"
            )
        )

        // Convierte cada BookItem en una fila del Cursor
        libros.forEach { libro ->
            val info = libro.volumeInfo

            cursor.addRow(
                arrayOf(
                    libro.id,
                    info.title,
                    info.authors?.joinToString(", ") ?: "Autor desconocido",
                    info.description ?: "Sin descripción",
                    info.imageLinks?.secureThumbnail ?: ""
                )
            )
        }

        return cursor
    }

    override fun getType(uri: Uri): String = "vnd.android.cursor.dir/vnd.biblioteca.libro"
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
}