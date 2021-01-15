package com.mauricio.pokemon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mauricio.pokemon.pokemon.models.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.pokemon.repository.PokemonRepository
import com.mauricio.pokemon.pokemon.viewmodel.PokemonViewModel
import com.mauricio.pokemon.rules.RxSchedulersOverrideRule
import junit.framework.Assert.assertNotNull
import org.junit.*
import org.junit.Assert.assertEquals
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import javax.inject.Inject

class ConversionUnitTest {

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxSchedulersOverrideRule()
    }
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()
    @Mock
    lateinit var viewModel: PokemonViewModel
    @Mock
    private lateinit var mockContext: Application
    @Mock
    @Inject lateinit var pokemonRepository: PokemonRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
//        pokemonRepository = PokemonRepository(mockContext)
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
        assertNotNull(viewModel.pokemons.value)
        assertEquals(TOTAL_INICIAL_POKEMONS, viewModel.pokemons.value?.size)
    }
}