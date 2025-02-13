package com.example.trendify.ui.home.fragments.category

import Constants
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trendify.R
import com.example.trendify.databinding.FragmentCategoryBinding
import com.example.trendify.ui.home.fragments.news.NewsFragment

class CategoryFragment : Fragment() {
    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    val newsFragment = NewsFragment()
    private var categorySelectedListener: OnCategorySelected? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRv()
        categorySelectedListener = context as OnCategorySelected
    }

    private fun initRv() {
        val adapter = CategoriesAdapter {
            val bundle = Bundle()
            bundle.putString(Constants.CATEGORY_ID, it.id)
            categorySelectedListener?.onCategorySelected(getString(it.title))
            newsFragment.arguments = bundle
            parentFragmentManager.beginTransaction().replace(R.id.fragment_container, newsFragment)
                .commit()
        }
        binding.categoriesRV.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    interface OnCategorySelected {
        fun onCategorySelected(selectedCategory: String)
    }
}