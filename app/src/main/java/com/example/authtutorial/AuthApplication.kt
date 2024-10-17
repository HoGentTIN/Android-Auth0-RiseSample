package com.example.authtutorial

import android.app.Application
import com.example.authtutorial.DI.AppContainer
import com.example.authtutorial.DI.DefaultAppContainer

class AuthApplication: Application() {
    lateinit var container : AppContainer

    override fun onCreate() {
        super.onCreate()
        //DI
        container = DefaultAppContainer(context = applicationContext)
    }
}