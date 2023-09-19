package com.fulbiopretell.retoyape.source

interface IRepositoryApp {
    suspend fun getRecipes(): RepositoryApp.State
}