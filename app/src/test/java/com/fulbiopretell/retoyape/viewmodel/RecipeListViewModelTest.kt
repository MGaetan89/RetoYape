package com.fulbiopretell.retoyape.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.fulbiopretell.retoyape.model.Recipe
import com.fulbiopretell.retoyape.source.RepositoryApp
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class RecipeListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var repositoryApp: RepositoryApp
    private lateinit var viewModel: RecipeListViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        repositoryApp = mockk()
        viewModel = RecipeListViewModel(repositoryApp)
    }

    @Test
    fun `when getRecipes is called, it emits Loading then ListRecipesLoaded`() =
        runTest {
            val mockList = listOf<Recipe>(
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
            val successState = RepositoryApp.State.Success(mockList)

            coEvery { repositoryApp.getRecipes() } returns successState

            val observer = mockk<Observer<RecipeListViewModel.ViewState>>(relaxed = true)

            viewModel.getRecipes().observeForever(observer)

            verify {
                observer.onChanged(RecipeListViewModel.ViewState.Loading)
                observer.onChanged(RecipeListViewModel.ViewState.ListRecipesLoaded(mockList))
            }
        }

    @Test
    fun `when getRecipes fails, it emits Loading then ListRecipesFailure`() =
        runTest {
            val errorMessage = "Error"
            coEvery { repositoryApp.getRecipes() } throws Exception(errorMessage)

            val observer = mockk<Observer<RecipeListViewModel.ViewState>>(relaxed = true)

            viewModel.getRecipes().observeForever(observer)

            verify {
                observer.onChanged(RecipeListViewModel.ViewState.Loading)
                observer.onChanged(RecipeListViewModel.ViewState.ListRecipesFailure(errorMessage))
            }
        }
}
