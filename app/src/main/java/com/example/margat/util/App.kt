package com.example.margat.util

import android.app.Application
import android.content.Context

class App: Application() {

    override fun onCreate(){
        super.onCreate()
        MyApp.context = applicationContext
    }

    object MyApp {
        lateinit var context: Context

        fun getAppContext(): Context {
            return context
        }
    }
}