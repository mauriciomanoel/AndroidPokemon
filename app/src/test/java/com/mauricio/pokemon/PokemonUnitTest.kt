package com.mauricio.pokemon

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mauricio.pokemon.pokemon.models.TOTAL_INICIAL_POKEMONS
import com.mauricio.pokemon.pokemon.viewmodel.PokemonViewModel
import com.mauricio.pokemon.rules.RxImmediateSchedulerRule
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations


class PokemonUnitTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    companion object {
        @ClassRule
        @JvmField
        val schedulers = RxImmediateSchedulerRule()
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