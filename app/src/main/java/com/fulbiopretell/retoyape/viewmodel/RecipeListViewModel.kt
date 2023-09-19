package com.fulbiopretell.retoyape.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.fulbiopretell.retoyape.model.Recipe
import com.fulbiopretell.retoyape.source.RepositoryApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class RecipeListViewModel @Inject constructor(
    private val repositoryApp: RepositoryApp
) : ViewModel()  {

    fun getRecipes() = liveData(Dispatchers.IO) {
        emit(ViewState.Loading)
        try {
            val getRecipesResponse = repositoryApp.getRecipes() as RepositoryApp.State.Success
            emit(ViewState.ListRecipesLoaded(getRecipesResponse.response))
        } catch (e: Exception) {
            emit(ViewState.ListRecipesFailure(e.message ?: "Error"))
            Timber.e(e)
        }
    }

    sealed class ViewState() {
        object Loading : ViewState()
        data class ListRecipesLoaded(val listRecipes: List<Recipe>) : ViewState()
        data class ListRecipesFailure(val error: String) : ViewState()
    }
}