package com.mauricio.pokemon.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.mauricio.pokemon.models.pokemon.*
import com.mauricio.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.viewmodel.BaseViewModel

class MainViewModel(application: Application): BaseViewModel(application) {}