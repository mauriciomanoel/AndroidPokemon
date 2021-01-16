package com.mauricio.pokemon.di.component

import android.app.Application
import com.mauricio.pokemon.PokemonApplication
import com.mauricio.pokemon.di.module.ActivityBindings
import com.mauricio.pokemon.di.module.AppModule
import com.mauricio.pokemon.di.module.FragmentBindings
import com.mauricio.pokemon.di.module.NetworkModule
import com.mauricio.pokemon.pokemon.viewmodel.PokemonViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, ActivityBindings::class, FragmentBindings::class, AndroidInjectionModule::class])
interface AppComponent : AndroidInjector<PokemonApplication> {
    fun inject(application: Application)
    fun inject(viewModel:PokemonViewModel)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(application: Application): Builder
        fun build(): AppComponent
    }
}
