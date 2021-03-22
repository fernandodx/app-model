package br.com.diasdeseries

import android.app.Application

class DiasdeSeriesApplication : Application(){

    lateinit var appComponent: MyApplicationComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerMyApplicationComponent.factory().create(this)
    }



}