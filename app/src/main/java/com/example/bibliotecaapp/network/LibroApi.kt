package com.example.bibliotecaapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


data class LibroApi(

    /*
    * RESPUESTA DE LA API DE BOOKS
    * LISTA DE RESULTADOS
    * */

    val items: List<BookItem>?
)

data class BookItem(

    /*
    * ITEM INDIVIDUAL DE LA LISTA
    * CONTIENE ID Y LA INFO DEL VOLUMEN
    * */

    val id: String,
    val volumeInfo: VolumeInfo
)

data class VolumeInfo(

    /*
    * DETALLES DEL LIBRO
    * TITULO AUTOR Y PORTADA
    * */

    val title: String,
    val authors: List<String>?,
    val description: String?,
    val imageLinks: ImageLinks?
)

data class ImageLinks(

    /*
    * URLS DE LAS PORTADAS DEL LIBRO
    * */

    val thumbnail: String?
) {
    val secureThumbnail: String?
        get() = thumbnail?.replace("http://", "https://")
}


interface BookApi{

    /*
    * INTERFAZ RETROFIT
    * CONSULTA A LA API DE GOOGLE
    * */

    @GET("volumes")
    suspend fun obtenerLibros(
        @Query("q") busqueda:String,
        @Query("key")apiKey:String
    ): LibroApi

}

object RetrofitClient {

    /*
    * CLIENTE RETROFIT
    * INSTANCIA DE BASE URL Y GSON
    * */

    private const val BASE_URL = "https://www.googleapis.com/books/v1/"

    val instance: BookApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BookApi::class.java)
    }
}