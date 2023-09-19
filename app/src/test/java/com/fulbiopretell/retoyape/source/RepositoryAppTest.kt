package com.fulbiopretell.retoyape.source

import com.fulbiopretell.retoyape.model.Recipe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class RepositoryAppTest {

    // Mockeo de ApiService
    private val apiService: ApiService = mockk()

    // Instancia del RepositoryApp
    private lateinit var repositoryApp: RepositoryApp

    @Before
    fun setup() {
        // Inicialización del RepositoryApp con el mock de ApiService
        repositoryApp = RepositoryApp(apiService)
    }

    @Test
    fun `getRecipes returns State Success when apiService call is successful`() = runBlocking {
        // Given
        val mockResponse = listOf(
            Recipe(
            "https://firebasestorage.googleapis.com/v0/b/flashbook-78613.appspot.com/o/recipes%2Fceviche.jpeg?alt=media&token=cac8535b-bdb3-423b-9055-2b54855c09f3",
            "Ceviche",
            "El ceviche es una de las piedras angulares de la gastronomía peruana. Este plato de pescado crudo se marina en jugo de limón y se mezcla con cebolla roja, ají y cilantro. Se sirve generalmente con camote y maíz tostado.",
            -12.0464,
            -77.0428
        ),
            Recipe(
                "https://firebasestorage.googleapis.com/v0/b/flashbook-78613.appspot.com/o/recipes%2Flomo%20saltado.jpg?alt=media&token=882e0f84-81fe-4ed1-bbf0-dc9a364b6123",
                "Lomo Saltado",
                "El lomo saltado es una fusión de sabores peruanos y chinos. Consiste en tiras de carne de res salteadas con cebollas, tomates, ajíes y se sazona con soja y vinagre. Es común acompañar este plato con arroz y papas fritas.",
                -12.0464,
                -77.0428
            )
        )
        coEvery { apiService.getRecipes() } returns Response.success(mockResponse)

        // When
        val result = repositoryApp.getRecipes()

        // Then
        assertEquals(RepositoryApp.State.Success(mockResponse), result)
    }

    @Test
    fun `getRecipes returns State Failure when apiService call is unsuccessful`() = runBlocking {
        // Given
        val mockError = "An error occurred"
        coEvery { apiService.getRecipes() } returns Response.error(400, ResponseBody.create(null, mockError))

        // When
        val result = repositoryApp.getRecipes()

        // Then
        assertEquals(RepositoryApp.State.Failure(mockError), result)
    }
}
