package com.example.trendify.ui.home

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.trendify.R
import com.example.trendify.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    lateinit var navHostFragment: NavHostFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDrawer()
        initSpinners()
        clearNews()
    }

    private fun initDrawer() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.categoryFragment, R.id.newsFragment),
            binding.drawerLayout
        )
        binding.appBar.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun initSpinners() {
        val themesAdapter =
            ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.themes))
        val langAdapter =
            ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.languages))
        binding.themeSpinner.adapter = themesAdapter
        binding.languageSpinner.adapter = langAdapter
    }




    fun clearNews() {
        binding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.categoryFragment -> {
                    navController.navigate(
                        R.id.categoryFragment, null,
                        NavOptions.Builder()
                            .setPopUpTo(R.id.newsFragment, true) // Clears NewsFragment
                            .build()
                    )
                    binding.drawerLayout.closeDrawers()
                    true
                }
                else -> false
            }
        }
    }

}