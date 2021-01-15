package com.mauricio.pokemon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mauricio.pokemon.main.models.Constant
import com.mauricio.pokemon.network.RetrofitApiService
import com.mauricio.pokemon.pokemon.models.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.pokemon.viewmodel.PokemonViewModel
import com.mauricio.pokemon.rules.RxImmediateSchedulerRule
import com.mauricio.pokemon.rules.RxSchedulersOverrideRule
import junit.framework.Assert.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@RunWith(MockitoJUnitRunner::class)
class PokemonUnitTest {

//    @Rule
//    @JvmField
//    val rule = InstantTaskExecutorRule()
//
//    @Rule @JvmField
//    var mockitoRule = MockitoJUnit.rule()
//
//    @Rule @JvmField var testSchedulerRule = RxSchedulersOverrideRule()
//
//
//    @Mock
//    lateinit var retrofitApiService: RetrofitApiService
//    @Mock
//    lateinit var loggingInterceptor: HttpLoggingInterceptor
//    @Mock
//    lateinit var pokemonRepository: PokemonRepository


    // A JUnit Test Rule that swaps the background executor used by
    // the Architecture Components with a different one which executes each task synchronously.
    // You can use this rule for your host side tests that use Architecture Components.
    @Rule
    @JvmField
    var rule = InstantTaskExecutorRule()

    // Test rule for making the RxJava to run synchronously in unit test
    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
    }

    @Mock
    private lateinit var mockContext: Application

    @Mock
    lateinit var pokemonRepository: PokemonRepository


    lateinit var viewModel: PokemonViewModel


    @Before
    fun setup() {
//        MockitoAnnotations.initMocks(this)
        viewModel = PokemonViewModel(mockContext, pokemonRepository)
    }

    @Test
    fun checkIfVariablesShouldNotBeNull() {
        assertNotNull(viewModel)
        assertNotNull(pokemonRepository)
    }
    @Test
    fun callServiceAndReturnListOfPokemons() {
        viewModel.getPokemons()
        assertNotNull(viewModel)
        assertNotNull(viewModel.pokemons)
        assertEquals(TOTAL_INICIAL_POKEMONS, viewModel.pokemons.value?.size)
    }
}