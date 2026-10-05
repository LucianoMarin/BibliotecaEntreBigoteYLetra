package com.example.bibliotecaapp.utils

import com.example.bibliotecaapp.data.Libro
import com.example.bibliotecaapp.data.Usuario
import com.example.bibliotecaapp.ui.screens.com.example.bibliotecaapp.data.Socio
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ValidatorTest {

    /*
    * PRUEBAS UNITARIAS DE VALIDACIONES
    * TESTEO DE LOGICA Y FILTROS
    * */

    private lateinit var validator: Validator
    private lateinit var usuarios: MutableList<Usuario>
    private lateinit var libros: MutableList<Libro>

    @Before
    fun setUp() {

        /*
        * CONFIGURACION DE DATOS PRUEBA
        * CARGA DE USUARIOS Y LIBROS
        * */

        validator = Validator()

        val socio1 = Socio(
            rut = "18775116-0",
            primer_nombre = "Luciano",
            segundo_nombre = "Romario",
            apellido_paterno = "Marin",
            apellido_materno = "Villanueva",
            estado = true
        )

        val socio2 = Socio(
            rut = "9415449-9",
            primer_nombre = "Lujana",
            segundo_nombre = "Eunice",
            apellido_paterno = "Villanueva",
            apellido_materno = "Yavar",
            estado = true
        )

        usuarios = mutableListOf(
            Usuario("lmarin", "123", true, socio1),
            Usuario("lvillanueva", "456", true, socio2)
        )

        libros = mutableListOf(
            Libro("010101", "Metamorfosis", "Descripcion", "Kafka", "Disponible"),
            Libro("020202", "El Tunel", "Descripcion", "Sabato", "Prestado"),
            Libro("030303", "Rayuela", "Descripcion", "Cortazar", "Disponible"),
            Libro("040404", "Ficciones", "Descripcion", "Borges", "Prestado")
        )
    }

    @Test
    fun loginConCredencialesCorrectasDevuelveTrue() {

        /*
        * VALIDA LOGIN EXITOSO
        * */

        val resultado = validator.validarLogin(usuarios, "lmarin", "123")
        assertTrue(resultado)
    }

    @Test
    fun loginConClaveIncorrectaDevuelveFalse() {

        /*
        * RECHAZA CLAVE INCORRECTA
        * */

        val resultado = validator.validarLogin(usuarios, "lmarin", "999")
        assertFalse(resultado)
    }

    @Test
    fun loginConUsuarioInexistenteDevuelveFalse() {

        /*
        * RECHAZA USUARIOS NO REGISTRADOS
        * */

        val resultado = validator.validarLogin(usuarios, "admin", "123")
        assertFalse(resultado)
    }

    @Test
    fun loginConListaVaciaDevuelveFalse() {

        /*
        * MANEJO DE LISTA DE USUARIOS VACIA
        * */

        val resultado = validator.validarLogin(emptyList(), "lmarin", "123")
        assertFalse(resultado)
    }

    @Test
    fun registroConCamposVaciosDevuelveMensaje() {

        /*
        * VALIDA FORMATO DE CAMPOS REQUERIDOS
        * */

        val mensaje = validator.validarRegistro(usuarios, "", "")
        assertEquals("Complete todos los campos", mensaje)
    }

    @Test
    fun registroConUsuarioExistenteDevuelveMensaje() {

        /*
        * EVITA DUPLICIDAD DE NOMBRES DE USUARIO
        * */

        val mensaje = validator.validarRegistro(usuarios, "lmarin", "789")
        assertEquals("El usuario ya existe", mensaje)
    }

    @Test
    fun registroValidoDevuelveVacio() {

        /*
        * CONFIRMA REGISTRO CORRECTO
        * */

        val mensaje = validator.validarRegistro(usuarios, "nuevo", "789")
        assertEquals("", mensaje)
    }

    @Test
    fun rutVacioDevuelveFalse() {

        /*
        * RECHAZA STRINGS DE RUT VACIOS
        * */

        assertFalse(validator.validarRut(""))
    }

    @Test
    fun rutMuyCortoDevuelveFalse() {

        /*
        * CONTROLA LARGO MINIMO DE RUT
        * */

        assertFalse(validator.validarRut("123"))
    }

    @Test
    fun rutValidoDevuelveTrue() {

        /*
        * VERIFICA FORMATO CORRECTO DE RUT
        * */

        assertTrue(validator.validarRut("18775116-0"))
    }

    @Test
    fun nombreVacioDevuelveFalse() {

        /*
        * RECHAZA NOMBRES VACIOS
        * */

        assertFalse(validator.validarNombre(""))
    }

    @Test
    fun nombreConNumerosDevuelveFalse() {

        /*
        * EVITA NUMEROS EN CAMPO DE NOMBRE
        * */

        assertFalse(validator.validarNombre("Luciano123"))
    }

    @Test
    fun nombreSoloLetrasDevuelveTrue() {

        /*
        * VALIDA NOMBRES SOLO CON LETRAS
        * */

        assertTrue(validator.validarNombre("Luciano"))
    }

    @Test
    fun nombreConEspacioDevuelveTrue() {

        /*
        * ADMITE COMPUESTOS Y ESPACIOS
        * */

        assertTrue(validator.validarNombre("Luciano Romario"))
    }

    @Test
    fun contarDisponiblesDevuelveDos() {

        /*
        * CONTEO DE LIBROS EN STOCK
        * */

        val total = validator.contarDisponibles(libros)
        assertEquals(2, total)
    }

    @Test
    fun contarPrestadosDevuelveDos() {

        /*
        * CONTEO DE LIBROS PRESTADOS
        * */

        val total = validator.contarPrestados(libros)
        assertEquals(2, total)
    }

    @Test
    fun filtrarDisponiblesDevuelveSoloDisponibles() {

        /*
        * FILTRADO POR ESTADO DISPONIBLE
        * */

        val resultado = validator.filtrarDisponibles(libros)
        assertEquals(2, resultado.size)
        assertTrue(resultado.all { it.estado == "Disponible" })
    }

    @Test
    fun filtrarPrestadosDevuelveSoloPrestados() {

        /*
        * FILTRADO POR ESTADO PRESTADO
        * */

        val resultado = validator.filtrarPrestados(libros)
        assertEquals(2, resultado.size)
        assertTrue(resultado.all { it.estado == "Prestado" })
    }

    @Test
    fun buscarPorTituloEncuentraCoincidencias() {

        /*
        * BUSQUEDA POR PALABRAS CLAVE
        * */

        val resultado = validator.buscarPorTitulo(libros, "rayuela")
        assertEquals(1, resultado.size)
    }

    @Test
    fun buscarPorTituloSinCoincidenciasDevuelveVacio() {

        /*
        * RETORNA LISTA VACIA SI NO HAY RESULTADOS
        * */

        val resultado = validator.buscarPorTitulo(libros, "zzzz")
        assertTrue(resultado.isEmpty())
    }
}