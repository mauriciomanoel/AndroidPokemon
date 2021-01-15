package com.mauricio.pokemon.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import com.google.gson.Gson
import com.mauricio.pokemon.R
import com.mauricio.pokemon.pokemondetail.view.DetailPokemonActivity
import com.mauricio.pokemon.databinding.ActivityMainBinding
import com.mauricio.pokemon.pokemon.view.HomeFragment
import com.mauricio.pokemon.main.models.ScreenControlEnum
import com.mauricio.pokemon.main.interfaces.IOnClickEvent
import com.mauricio.pokemon.pokemon.models.Pokemon

class MainActivity : AppCompatActivity(), IOnClickEvent {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by lazy { ViewModelProviders.of(this).get(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)
        switchFragment(ScreenControlEnum.HOME)
    }

    /**
     * Switch fragment.
     *
     * @param screen            the screen
     * @param addToBackStack    the add to back stack
     */
    fun switchFragment(screen: ScreenControlEnum?, addToBackStack: Boolean = true) {
        val fragmentManager = supportFragmentManager
        var fragment: Fragment? = null
        var fragmentName: String? = null
        val bundle = Bundle()
        if (!addToBackStack) {
            clearStack()
        }

        when (screen) {
            ScreenControlEnum.HOME -> {
                fragment = HomeFragment.newInstance()
                fragmentName = screen.title
                fragment.arguments = bundle
            }
        }

        fragment?.let { frag ->
            if (addToBackStack) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_private_frame, frag, fragmentName)
                    .addToBackStack(fragmentName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            } else {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.content_private_frame, frag, fragmentName)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit()
            }
        }
    }

    fun clearStack() { //Here we are clearing back stack fragment entries
        val fm: FragmentManager = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
        //Here we are removing all the fragment that are shown here
        if (supportFragmentManager.fragments.size > 0) {
            for (i in 0 until supportFragmentManager.fragments.size) {
                val mFragment = supportFragmentManager.fragments[i]
                if (mFragment != null) {
                    supportFragmentManager.beginTransaction().remove(mFragment).commit()
                }
            }
        }
    }

    override fun onClickDetailPokemon(pokemon: Pokemon) {
        val intent = Intent(this, DetailPokemonActivity::class.java)
        intent.putExtra(Pokemon.TAG, Gson().toJson(pokemon))
        startActivity(intent)
    }

    override fun showLoading() {
        viewModel.showLoading()
    }

    override fun hideLoading() {
        viewModel.hideLoading()
    }

    companion object {

        val TAG = MainActivity::class.java.name

    }
}