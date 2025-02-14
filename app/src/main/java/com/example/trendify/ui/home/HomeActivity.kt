package com.example.trendify.ui.home

import Constants
import SharedPrefManager
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.trendify.R
import com.example.trendify.api.model.Category
import com.example.trendify.databinding.ActivityHomeBinding
import com.example.trendify.ui.home.fragments.category.CategoryFragment
import com.example.trendify.ui.home.fragments.news.NewsFragment

class HomeActivity : AppCompatActivity(), CategoryFragment.OnCategorySelected {

    lateinit var binding: ActivityHomeBinding
    var category: String = "general"
    private val categoryFragment = CategoryFragment()
    private val newsFragment = NewsFragment()

    override fun onStart() {
        super.onStart()
        initPref()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        SharedPrefManager.init(this)
        if (savedInstanceState == null) {
            showFragment(categoryFragment)
        }
        initUI()
    }


    // Handle UI

    private fun initUI() {
        initSpinners()
        initSearch()
        openDrawer()
        navigation()
    }


    // Handle App Preferences

    private fun initPref() {
        initLanguage()
        initTheme()
    }

    private fun initLanguage() {
        val language: String = SharedPrefManager.get(Constants.LANGUAGE, "en")
        changeLanguage(language)
    }

    private fun initTheme() {
        val theme: Int = SharedPrefManager.get(Constants.THEME, AppCompatDelegate.MODE_NIGHT_NO)
        changeTheme(theme)
    }

    // Handle Search Bar

    private fun initSearch() {
        val searchBar = binding.appBar.searchBar
        binding.appBar.searchIcon.setOnClickListener {
            toggleSearchIcon(searchBar)
            handleSearch()
        }
    }

    private fun handleSearch() {
        val searchBar = binding.appBar.searchBar
        searchBar.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE ||
                (event?.keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_DOWN)
            ) {
                val query = searchBar.text.toString().trim()
                if (query.isNotEmpty()) {
                    val bundle = Bundle().apply {
                        putString(Constants.SEARCH, query)
                        putString(Constants.CATEGORY_ID, category)
                    }
                    newsFragment.arguments = bundle
                    showFragment(newsFragment)
                    binding.appBar.searchIcon.callOnClick()
                } else {
                    Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
                }
                true
            } else {
                false
            }
        }
    }

    private fun toggleSearchIcon(searchBar: EditText) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val icon = binding.appBar.searchIcon
        if (searchBar.isVisible) {
            searchBar.isVisible = false
            searchBar.text = null
            imm.hideSoftInputFromWindow(searchBar.windowToken, 0)
            icon.setImageResource(R.drawable.search)
        } else {
            searchBar.isVisible = true
            searchBar.requestFocus()
            imm.showSoftInput(searchBar, InputMethodManager.SHOW_IMPLICIT)
            icon.setImageResource(R.drawable.close_ic)
        }
    }

    // Handle Drawer

    private fun openDrawer() {
        binding.appBar.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }
    }

    private fun initSpinners() {
        val themesAdapter =
            ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.themes))
        val langAdapter =
            ArrayAdapter(this, R.layout.spinner_item, resources.getStringArray(R.array.languages))
        val themeSpinner = binding.themeSpinner
        themeSpinner.adapter = themesAdapter
        val languageSpinner = binding.languageSpinner
        languageSpinner.adapter = langAdapter
        val themePosition = SharedPrefManager.get(Constants.THEME, 1) - 1
        themeSpinner.setSelection(themePosition)
        val language = when (SharedPrefManager.get(Constants.LANGUAGE, "en")) {
            "en" -> "English"
            "ar" -> "Arabic"
            "es" -> "Spanish"
            "fr" -> "French"
            else -> "English"
        }
        val langPosition = langAdapter.getPosition(language)
        languageSpinner.setSelection(langPosition)
        setupTheme()
        setupLanguage()
    }

    // Handle Navigate Home Fragment

    private fun navigation() {
        binding.navView.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.categoryFragment -> {
                    supportFragmentManager.popBackStack(
                        null,
                        androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
                    )
                    showFragment(categoryFragment, true)
                    binding.drawerLayout.closeDrawers()
                    updateTitle(getString(R.string.home))
                    return@setNavigationItemSelectedListener true
                }
            }
            return@setNavigationItemSelectedListener false
        }
    }

    // Handle Fragment Transactions

    private fun showFragment(fragment: Fragment, backStack: Boolean = false) {
        val transaction = supportFragmentManager.beginTransaction()

        if (fragment is CategoryFragment) {
            transaction.replace(R.id.fragment_container, fragment)
            binding.navView.menu.getItem(0).isChecked = true
        } else if (backStack) {
            binding.navView.menu.getItem(0).isChecked = false
            transaction.replace(R.id.fragment_container, fragment)
            transaction.addToBackStack(null)
        } else {
            binding.navView.menu.getItem(0).isChecked = false
            transaction.replace(R.id.fragment_container, fragment)
        }
        transaction.commit()
    }

    // Handle Appbar Title

    private fun updateTitle(title: String) {
        binding.appBar.title.text = title
    }

    // Handle Themes

    private fun setupTheme() {
        binding.themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (p2) {
                    0 -> changeTheme(AppCompatDelegate.MODE_NIGHT_NO)

                    1 -> changeTheme(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    private fun changeTheme(theme: Int) {
        AppCompatDelegate.setDefaultNightMode(theme)
        SharedPrefManager.put(Constants.THEME, theme)
    }

    // Handle Language

    private fun setupLanguage() {
        binding.languageSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    when (p2) {
                        0 -> changeLanguage("en")

                        1 -> changeLanguage("ar")

                        2 -> changeLanguage("es")

                        3 -> changeLanguage("fr")
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }
    }

    private fun changeLanguage(lang: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(lang))
        SharedPrefManager.put(Constants.LANGUAGE, lang)
    }

    // Handle Back Button

    override fun onBackPressed() {
        val fragmentManager = supportFragmentManager
        val currentFragment = fragmentManager.findFragmentById(R.id.fragment_container)
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawers()
        } else if (binding.appBar.searchBar.isVisible) {
            toggleSearchIcon(binding.appBar.searchBar)
        } else if (currentFragment is NewsFragment) {
            showFragment(categoryFragment)
            updateTitle(getString(R.string.home))
        } else {
            super.onBackPressed()
        }
    }

    // Send Selected Category Title to Home Activity

    override fun onCategorySelected(selectedCategory: Category) {
        updateTitle(getString(selectedCategory.title))
        category = selectedCategory.id
        binding.navView.menu.getItem(0).isChecked = false
    }

}
