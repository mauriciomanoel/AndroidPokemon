package com.mauricio.pokemon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mauricio.pokemon.pokemon.models.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.rules.RxSchedulersOverrideRule
import com.mauricio.pokemon.pokemon.viewmodel.PokemonViewModel
import junit.framework.Assert.assertNotNull
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class ExampleUnitTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxSchedulersOverrideRule()
    }

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    @Mock
    private lateinit var mockContext: Application
    @Mock
    private lateinit var viewModelPokemon: PokemonViewModel
    @Mock
    private lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        pokemonRepository = PokemonRepository.getInstance(mockContext)
        viewModelPokemon = PokemonViewModel(mockContext, pokemonRepository)
    }

    @Test
    fun checkIfVariablesShouldNotBeNull() {
        assertNotNull(viewModelPokemon)
        assertNotNull(pokemonRepository)
    }
    @Test
    fun callServiceAndReturnListOfPokemons() {
        viewModelPokemon.getPokemons()
        assertNotNull(viewModelPokemon.pokemons.value)
        assertEquals(TOTAL_INICIAL_POKEMONS, viewModelPokemon.pokemons.value?.size)
    }
}