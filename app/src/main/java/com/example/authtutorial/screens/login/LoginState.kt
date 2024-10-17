package com.example.authtutorial.screens.login

data class LoginState(val username: String = "",
    val password: String = "", val loginError: Boolean = false)
