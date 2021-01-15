package com.mauricio.pokemon.di.module

import android.app.Application
import com.mauricio.pokemon.network.RetrofitApiService
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

//    @Provides
//    @Singleton
//    fun providesPreferenceUtils(application: Application, context: Context): SharedPreferences =
//            PreferenceManager.getDefaultSharedPreferences(application)
//
//    @Provides
//    @Singleton
//    fun providesResources(application: Application, context: Context): Resources = application.resources

    @Provides
    @Singleton
    fun provideRestService(application: Application, apiService: RetrofitApiService) = PokemonRepository(application, apiService)

}
