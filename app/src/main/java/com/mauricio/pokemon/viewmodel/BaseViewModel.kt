package com.mauricio.pokemon.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {

    private val compositeDisposable: CompositeDisposable?
    val showLoading = MutableLiveData<Boolean>(false)

    init {
        compositeDisposable = CompositeDisposable()
    }

    fun addDisposable(d: Disposable) {
        compositeDisposable?.add(d)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable?.clear()
    }

    fun showLoading() {
        showLoading.postValue(true)
    }

    fun hideLoading() {
        showLoading.postValue(false)
    }
}