package com.example.authtutorial.DI

import android.content.Context
import com.example.authtutorial.network.auth0.Auth0Api
import com.example.authtutorial.repository.AuthRepo
import com.example.authtutorial.repository.IAuthRepo
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit

interface AppContainer {
    val authApiService: Auth0Api
    val authRepo: IAuthRepo
}

/* container that takes care of dependencies
It creates the api service and injects it into the repo.
*/
class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = "https://dev-387gw3fkqtq50fsu.us.auth0.com"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(
            GsonConverterFactory.create()
        )
        .baseUrl(baseUrl)
        .build()

    override val authApiService: Auth0Api by lazy {
        retrofit.create(Auth0Api::class.java)
    }
    override val authRepo: IAuthRepo by lazy {
        AuthRepo(authApiService)
    }
}
