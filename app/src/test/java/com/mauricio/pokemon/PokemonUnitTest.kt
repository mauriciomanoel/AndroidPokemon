package com.mauricio.pokemon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @Rule @JvmField
    var mockitoRule = MockitoJUnit.rule()

    @Rule @JvmField var testSchedulerRule = RxSchedulersOverrideRule()

    lateinit var viewModel: PokemonViewModel
    @Mock
    lateinit var retrofitApiService: RetrofitApiService
    @Mock
    lateinit var loggingInterceptor: HttpLoggingInterceptor
    @Mock
    lateinit var pokemonRepository: PokemonRepository
    @Mock
    private lateinit var mockContext: Application


    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val okHttpClient = okhttp3.OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
        retrofitApiService = retrofit.create(RetrofitApiService::class.java)
//        pokemonRepository = PokemonRepository(mockContext, retrofitApiService)
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