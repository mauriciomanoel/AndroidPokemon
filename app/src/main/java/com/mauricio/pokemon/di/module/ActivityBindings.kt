package com.mauricio.pokemon.di.module

import com.mauricio.pokemon.di.scope.ActivityScope
import com.mauricio.pokemon.di.scope.FragmentScope
import com.mauricio.pokemon.main.MainActivity
import com.mauricio.pokemon.pokemon.view.HomeFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityBindings {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): MainActivity
}
