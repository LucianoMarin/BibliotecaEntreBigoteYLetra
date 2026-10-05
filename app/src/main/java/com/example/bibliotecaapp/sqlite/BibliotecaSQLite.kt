package com.example.bibliotecaapp.data.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.bibliotecaapp.data.Libro
import com.example.bibliotecaapp.data.Usuario
import com.example.bibliotecaapp.ui.screens.com.example.bibliotecaapp.data.Socio

class BibliotecaSQLite(context: Context) :

/*
* HELPER DE LA BASE DE DATOS SQLITE
* GESTIONA LAS TABLAS Y CONSULTAS DEL SISTEMA
* */

    SQLiteOpenHelper(context, "biblioteca.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {

        /*
        * CREACION DE TABLAS E INSERCION DE DATOS
        * */

        db.execSQL(
            """
            CREATE TABLE usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                usuario TEXT NOT NULL,
                clave TEXT NOT NULL,
                estado_cuenta INTEGER NOT NULL,
                rut TEXT,
                primer_nombre TEXT,
                segundo_nombre TEXT,
                apellido_paterno TEXT,
                apellido_materno TEXT,
                estado_socio INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE libros (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                codigo TEXT NOT NULL UNIQUE,
                titulo TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                autor TEXT NOT NULL,
                estado TEXT NOT NULL
            )
            """.trimIndent()
        )

        insertarUsuarioInicial(db, "lmarin", "123", true, "18775116-0", "Luciano", "Romario", "Marin", "Villanueva", true)
        insertarUsuarioInicial(db, "lvillanueva", "123", true, "9415449-9", "Lujana", "Eunice", "Villanueva", "Yavar", true)
        insertarUsuarioInicial(db, "mmarin", "123", true, "9862879-7", "Mario", "Baltazar", "Marin", "Reyes", true)
        insertarUsuarioInicial(db, "jcarrasco", "123", true, "19029771-3", "Jocelyn", "Carrasco", "Carrasco", "Carrasco", true)
        insertarUsuarioInicial(db, "prueba", "123", true, "1111111-1", "Prueba", "Prueba", "Prueba", "Prueba", true)

        insertarLibroInicial(db, "010101", "Metamorfosis", "Narra la tragedia de Gregorio Samsa, quien despierta convertido en un insecto y sufre el cruel rechazo de su propia familia al dejar de ser el sostén económico del hogar.", "Kafka", "Disponible")
        insertarLibroInicial(db, "020202", "Relatos Que miente un poco", "Un recorrido profundo por su vida y el mito del rock nacional, donde la memoria se mezcla con la música y la leyenda.", "Indio Solari", "Prestado")
        insertarLibroInicial(db, "030303", "Cien Años de Soledad", "La historia de la familia Buendía en el pueblo de Macondo, donde lo real y lo fantástico se mezclan a lo largo de varias generaciones.", "Gabriel García Márquez", "Disponible")
        insertarLibroInicial(db, "040404", "El Túnel", "Un pintor obsesionado con una mujer narra desde la cárcel los motivos que lo llevaron a cometer su crimen.", "Ernesto Sabato", "Prestado")
        insertarLibroInicial(db, "050505", "Rayuela", "Una novela que se puede leer de varias formas, siguiendo la vida de Horacio Oliveira entre París y Buenos Aires.", "Julio Cortázar", "Disponible")
        insertarLibroInicial(db, "060606", "La Casa de los Espíritus", "La saga de la familia Trueba a lo largo de cuatro generaciones, con un trasfondo político y sobrenatural.", "Isabel Allende", "Prestado")
        insertarLibroInicial(db, "070707", "Ficciones", "Colección de cuentos que juegan con laberintos, espejos, bibliotecas infinitas y el paso del tiempo.", "Jorge Luis Borges", "Disponible")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        /*
        * RESTRUCTURACION DE TABLAS AL CAMBIAR VERSION
        * */

        db.execSQL("DROP TABLE IF EXISTS usuarios")
        db.execSQL("DROP TABLE IF EXISTS libros")
        onCreate(db)
    }

    private fun insertarUsuarioInicial(
        db: SQLiteDatabase,
        usuario: String,
        clave: String,
        estadoCuenta: Boolean,
        rut: String,
        primerNombre: String,
        segundoNombre: String?,
        apellidoPaterno: String,
        apellidoMaterno: String,
        estadoSocio: Boolean?
    ) {

        /*
        * METODO INTERNO PARA CARGAR USUARIOS BASE
        * */

        db.execSQL(
            """
            INSERT INTO usuarios (
                usuario, clave, estado_cuenta, rut,
                primer_nombre, segundo_nombre,
                apellido_paterno, apellido_materno, estado_socio
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            arrayOf(
                usuario,
                clave,
                if (estadoCuenta) 1 else 0,
                rut,
                primerNombre,
                segundoNombre,
                apellidoPaterno,
                apellidoMaterno,
                if (estadoSocio == true) 1 else 0
            )
        )
    }

    private fun insertarLibroInicial(
        db: SQLiteDatabase,
        codigo: String,
        titulo: String,
        descripcion: String,
        autor: String,
        estado: String
    ) {

        /*
        * METODO INTERNO PARA CARGAR LIBROS BASE
        * */

        db.execSQL(
            """
            INSERT INTO libros (codigo, titulo, descripcion, autor, estado)
            VALUES (?, ?, ?, ?, ?)
            """.trimIndent(),
            arrayOf(codigo, titulo, descripcion, autor, estado)
        )
    }

    fun existeRut(rut: String): Boolean {

        /*
        * VALIDACION DE RUT REGISTRADO EN LA BD
        * */

        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id FROM usuarios WHERE rut = ? LIMIT 1",
            arrayOf(rut)
        )
        val existe = cursor.moveToFirst()
        cursor.close()
        return existe
    }

    fun validarUsuario(usuario: String, clave: String): String? {

        /*
        * COMPROBACION DE CREDENCIALES DE LOGIN
        * */

        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT primer_nombre, apellido_paterno
            FROM usuarios
            WHERE usuario = ? AND clave = ? AND estado_cuenta = 1
            """.trimIndent(),
            arrayOf(usuario, clave)
        )

        var nombreCompleto: String? = null

        if (cursor.moveToFirst()) {
            nombreCompleto = cursor.getString(0) + " " + cursor.getString(1)
        }

        cursor.close()
        return nombreCompleto
    }

    fun obtenerUsuario(usuario: String, clave: String): Usuario? {

        /*
        * OBTENCION DE DATOS COMPLETOS DE UN USUARIO
        * */

        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT usuario, clave, estado_cuenta, rut,
                   primer_nombre, segundo_nombre,
                   apellido_paterno, apellido_materno, estado_socio
            FROM usuarios
            WHERE usuario = ? AND clave = ?
            """.trimIndent(),
            arrayOf(usuario, clave)
        )

        var user: Usuario? = null

        if (cursor.moveToFirst()) {

            val socio = Socio(
                rut = cursor.getString(3),
                primer_nombre = cursor.getString(4),
                segundo_nombre = cursor.getString(5),
                apellido_paterno = cursor.getString(6),
                apellido_materno = cursor.getString(7),
                estado = cursor.getInt(8) == 1
            )

            user = Usuario(
                usuario = cursor.getString(0),
                clave = cursor.getString(1),
                estadoCuenta = cursor.getInt(2) == 1,
                socio = socio
            )
        }

        cursor.close()
        return user
    }

    fun listarUsuarios(): MutableList<Usuario> {

        /*
        * CONSULTA GENERAL DE TODOS LOS USUARIOS
        * */

        val lista = mutableListOf<Usuario>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            """
            SELECT usuario, clave, estado_cuenta, rut,
                   primer_nombre, segundo_nombre,
                   apellido_paterno, apellido_materno, estado_socio
            FROM usuarios
            """.trimIndent(),
            null
        )

        while (cursor.moveToNext()) {

            val socio = Socio(
                rut = cursor.getString(3),
                primer_nombre = cursor.getString(4),
                segundo_nombre = cursor.getString(5),
                apellido_paterno = cursor.getString(6),
                apellido_materno = cursor.getString(7),
                estado = cursor.getInt(8) == 1
            )

            lista.add(
                Usuario(
                    usuario = cursor.getString(0),
                    clave = cursor.getString(1),
                    estadoCuenta = cursor.getInt(2) == 1,
                    socio = socio
                )
            )
        }

        cursor.close()
        return lista
    }

    fun insertarUsuario(user: Usuario) {

        /*
        * REGISTRO DE UN NUEVO USUARIO EN LA BD
        * */

        val db = writableDatabase
        val socio = user.socio

        db.execSQL(
            """
            INSERT INTO usuarios (
                usuario, clave, estado_cuenta, rut,
                primer_nombre, segundo_nombre,
                apellido_paterno, apellido_materno, estado_socio
            )
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimIndent(),
            arrayOf(
                user.usuario,
                user.clave,
                if (user.estadoCuenta) 1 else 0,
                socio?.rut,
                socio?.primer_nombre,
                socio?.segundo_nombre,
                socio?.apellido_paterno,
                socio?.apellido_materno,
                if (socio?.estado == true) 1 else 0
            )
        )
    }

    fun listarLibros(): MutableList<Libro> {

        /*
        * OBTENER CATALOGO COMPLETO DE LIBROS
        * */

        val lista = mutableListOf<Libro>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT codigo, titulo, descripcion, autor, estado FROM libros",
            null
        )

        while (cursor.moveToNext()) {
            lista.add(
                Libro(
                    codigo = cursor.getString(0),
                    titulo = cursor.getString(1),
                    descripcion = cursor.getString(2),
                    autor = cursor.getString(3),
                    estado = cursor.getString(4)
                )
            )
        }

        cursor.close()
        return lista
    }

    fun obtenerLibro(codigo: String): Libro? {

        /*
        * BUSQUEDA DE LIBRO POR SU CODIGO
        * */

        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT codigo, titulo, descripcion, autor, estado FROM libros WHERE codigo = ?",
            arrayOf(codigo)
        )

        var libro: Libro? = null

        if (cursor.moveToFirst()) {
            libro = Libro(
                codigo = cursor.getString(0),
                titulo = cursor.getString(1),
                descripcion = cursor.getString(2),
                autor = cursor.getString(3),
                estado = cursor.getString(4)
            )
        }

        cursor.close()
        return libro
    }

    fun insertarLibro(libro: Libro) {

        /*
        * GUARDA UN LIBRO NUEVO EN LA TABLA
        * */

        val db = writableDatabase
        db.execSQL(
            """
            INSERT INTO libros (codigo, titulo, descripcion, autor, estado)
            VALUES (?, ?, ?, ?, ?)
            """.trimIndent(),
            arrayOf(
                libro.codigo,
                libro.titulo,
                libro.descripcion,
                libro.autor,
                libro.estado
            )
        )
    }

    fun actualizarLibro(libro: Libro): Boolean {

        /*
        * MODIFICACION DE DATOS DE UN LIBRO EXISTENTE
        * */

        val db = writableDatabase
        val filas = db.compileStatement(
            """
            UPDATE libros
            SET titulo = ?, descripcion = ?, autor = ?, estado = ?
            WHERE codigo = ?
            """.trimIndent()
        ).apply {
            bindString(1, libro.titulo)
            bindString(2, libro.descripcion)
            bindString(3, libro.autor)
            bindString(4, libro.estado)
            bindString(5, libro.codigo)
        }.executeUpdateDelete()

        return filas > 0
    }

    fun eliminarLibro(codigo: String): Boolean {

        /*
        * ELIMINA UN LIBRO SEGUN SU CODIGO
        * */

        val db = writableDatabase
        val filas = db.compileStatement(
            "DELETE FROM libros WHERE codigo = ?"
        ).apply {
            bindString(1, codigo)
        }.executeUpdateDelete()

        return filas > 0
    }

    fun prestarLibro(codigo: String): Boolean {

        /*
        * CAMBIA EL ESTADO DEL LIBRO A PRESTADO
        * */

        val db = writableDatabase
        val filas = db.compileStatement(
            """
            UPDATE libros
            SET estado = 'Prestado'
            WHERE codigo = ? AND estado = 'Disponible'
            """.trimIndent()
        ).apply {
            bindString(1, codigo)
        }.executeUpdateDelete()

        return filas > 0
    }

    fun devolverLibro(codigo: String): Boolean {

        /*
        * CAMBIA EL ESTADO DEL LIBRO A DISPONIBLE
        * */

        val db = writableDatabase
        val filas = db.compileStatement(
            """
            UPDATE libros
            SET estado = 'Disponible'
            WHERE codigo = ? AND estado = 'Prestado'
            """.trimIndent()
        ).apply {
            bindString(1, codigo)
        }.executeUpdateDelete()

        return filas > 0
    }
}