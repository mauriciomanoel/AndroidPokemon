package com.mauricio.pokemon.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel: ViewModel() {

    val showLoading = MutableLiveData<Boolean>(false)
    val messageError = MutableLiveData<String>()

    fun showLoading() {
        showLoading.postValue(true)
    }

    fun hideLoading() {
        showLoading.postValue(false)
    }
}