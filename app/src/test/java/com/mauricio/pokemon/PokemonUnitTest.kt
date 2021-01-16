package com.mauricio.pokemon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mauricio.pokemon.network.RetrofitApiService
import com.mauricio.pokemon.pokemon.models.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.pokemon.viewmodel.PokemonViewModel
import com.mauricio.pokemon.rules.RxImmediateSchedulerRule
import com.mauricio.pokemon.rules.RxSchedulersOverrideRule
import io.reactivex.Scheduler
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.disposables.Disposable
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor
import java.util.concurrent.TimeUnit


class PokemonUnitTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxSchedulersOverrideRule()
    }

    @Mock
    private lateinit var mockContext: Application

    lateinit var viewModel: PokemonViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
       viewModel = PokemonViewModel(mockContext)
    }

    @Test
    fun checkIfVariablesShouldNotBeNull() {
        assertNotNull(viewModel)
    }
    @Test
    fun callServiceAndReturnListOfPokemons() {
        viewModel.getPokemons()
        assertNotNull(viewModel.pokemons)
        assertEquals(TOTAL_INICIAL_POKEMONS, viewModel.pokemons.value?.size)
    }

}