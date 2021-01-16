package com.mauricio.pokemon.main

import android.app.Application
import javax.inject.Inject

//class MainViewModel(application: Application): BaseViewModel(application) {}

class MainViewModel @Inject constructor(application: Application) : BaseViewModel() {}