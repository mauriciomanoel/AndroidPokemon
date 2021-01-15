package com.mauricio.pokemon

import android.app.Application
import com.mauricio.pokemon.di.component.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class PokemonApplication : Application(), HasAndroidInjector {

    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .app(this)
            .build().inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}