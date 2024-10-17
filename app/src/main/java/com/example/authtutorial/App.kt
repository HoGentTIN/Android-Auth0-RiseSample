package com.example.authtutorial

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.example.authtutorial.network.auth0.AuthorizeResponse
import com.example.authtutorial.screens.login.Login
import com.example.authtutorial.screens.welcomeUser.WelcomeUser



@Composable
fun App(modifier: Modifier = Modifier) {
    var appState = rememberSaveable (stateSaver = AppStateSaver) {
        mutableStateOf(AppState())
    }



    if(!appState.value.isLoggedIn){
        Login(login = { authorizeResponse ->
            appState.value = appState.value.copy(isLoggedIn = true)
            Log.i("LOGIN", "App state login with token: " + authorizeResponse.access_token)
        })
    }
    else{
        WelcomeUser()
    }
}






