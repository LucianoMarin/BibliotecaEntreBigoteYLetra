package com.example.bibliotecaapp

import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LoginTest {

    /*
    * PRUEBAS INSTRUMENTADAS DE INTERFAZ
    * TESTEO DE FLUJOS DE LOGIN
    * */

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun esperarTexto(texto: String, timeout: Long = 5000) {

        /*
        * ESPERA QUE APAREZCA UN TEXTO EN PANTALLA
        * */

        composeTestRule.waitUntil(timeout) {
            composeTestRule
                .onAllNodesWithText(texto)
                .fetchSemanticsNodes()
                .isNotEmpty()
        }
    }

    @Before
    fun limpiarSesion() {

        /*
        * LIMPIA SHAREDPREFERENCES ANTES DE CADA PRUEBA
        * */

        val context = ApplicationProvider.getApplicationContext<Context>()
        context.getSharedPreferences("sesion", Context.MODE_PRIVATE)
            .edit().clear().commit()
    }

    @Test
    fun loginConCredencialesCorrectas() {

        /*
        * PRUEBA INGRESO EXITOSO CON CREDENCIALES VALIDAS
        * */

        esperarTexto("Login")
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.waitForIdle()

        val campos = composeTestRule.onAllNodes(hasSetTextAction())
        campos[0].performTextInput("lmarin")
        campos[1].performTextInput("123")

        composeTestRule.onNodeWithText("Ingresar").performClick()
        composeTestRule.waitForIdle()

        esperarTexto("Bienvenido a BiblioRED")
        composeTestRule.onAllNodesWithText("Bienvenido a BiblioRED")[0].assertIsDisplayed()
    }


    @Test
    fun loginConCamposVaciosMuestraError() {

        /*
        * PRUEBA VALIDACION DE CAMPOS VACIOS
        * */

        esperarTexto("Login")
        composeTestRule.onNodeWithText("Login").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithText("Ingresar").performClick()
        composeTestRule.waitForIdle()

        esperarTexto("Complete todos los campos")
        composeTestRule.onAllNodesWithText("Complete todos los campos")[0].assertIsDisplayed()
    }
}