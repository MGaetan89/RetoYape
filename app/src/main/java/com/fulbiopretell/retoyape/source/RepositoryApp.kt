package com.fulbiopretell.retoyape.source

import com.fulbiopretell.retoyape.model.Recipe
import javax.inject.Inject

class RepositoryApp @Inject constructor(private val apiService: ApiService) : IRepositoryApp {

    override suspend fun getRecipes(): State {

        val recipesListResponse = apiService.getRecipes()

        if (recipesListResponse.isSuccessful) {
            return State.Success(recipesListResponse.body() as List<Recipe>)
        } else {
            val message = recipesListResponse.errorBody()?.string() ?: recipesListResponse.message()
            return State.Failure(message)
        }
    }

    sealed class State {
        data class Success(val response: List<Recipe>) : State()
        data class Failure(val errorMessage: String) : State()
    }
}