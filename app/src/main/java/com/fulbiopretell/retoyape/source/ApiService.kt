package com.fulbiopretell.retoyape.source

import com.fulbiopretell.retoyape.model.Recipe
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("getRecipes/")
    suspend fun getRecipes(): Response<List<Recipe>>
}