package com.example.trendify.ui.home.fragments.news

import Constants
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.trendify.api.ApiManager
import com.example.trendify.api.model.ErrorResponse
import com.example.trendify.api.model.newsResponse.News
import com.example.trendify.api.model.newsResponse.NewsResponse
import com.example.trendify.api.model.sourcesResponse.Source
import com.example.trendify.api.model.sourcesResponse.SourcesResponse
import com.example.trendify.databinding.FragmentNewsBinding
import com.example.trendify.ui.home.fragments.articleDialog.ArticleDialogFragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsFragment : Fragment() {
    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!
    private val adapter: NewsAdapter = NewsAdapter {
        viewArticle(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSources()
        initViews()
    }

    private fun loadSources() {
        showLoading()
        val categoryId = arguments?.getString(Constants.CATEGORY_ID) ?: "general"
        ApiManager.webService().getSources(categoryId)
            .enqueue(object : Callback<SourcesResponse> {
                override fun onResponse(
                    call: Call<SourcesResponse>,
                    response: Response<SourcesResponse>
                ) {
                    if (!response.isSuccessful) {
                        val errorResponse =
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                        val message = errorResponse.message ?: "Something went wrong"
                        showError(message) {
                            binding.tryAgainButton.setOnClickListener {
                                loadSources()
                            }
                        }
                        return
                    }
                    bindTabLayout(response.body()?.sources)
                }

                override fun onFailure(call: Call<SourcesResponse>, error: Throwable) {
                    showError(error.message ?: "Something went wrong") {
                        loadSources()
                    }
                }

            })
    }

    private fun loadNews(sourceId: String?) {
        showLoading()
        val query = arguments?.getString(Constants.SEARCH)
        ApiManager.webService().getNews(source = sourceId ?: "abc-news", query)
            .enqueue(object : Callback<NewsResponse> {
                override fun onResponse(
                    call: Call<NewsResponse>,
                    response: Response<NewsResponse>
                ) {
                    if (!response.isSuccessful) {
                        val errorResponse =
                            Gson().fromJson(
                                response.errorBody()?.string(),
                                ErrorResponse::class.java
                            )
                        val message = errorResponse.message ?: "Something went wrong"
                        showError(message) {
                            binding.tryAgainButton.setOnClickListener({
                                loadNews(sourceId)
                            })
                        }
                        return
                    }
                    bindNewsList(response.body()?.newsList)
                }

                override fun onFailure(call: Call<NewsResponse>, ex: Throwable) {
                    val errorMessage = ex.message ?: "Something went wrong"
                    showError(errorMessage) {
                        loadNews(sourceId)
                    }
                }

            })
    }

    private fun bindTabLayout(sources: List<Source?>?) {
        showSuccess()
        sources?.forEach {
            val tab = binding.sourcesTab.newTab().setText(it?.name)
            tab.tag = it
            binding.sourcesTab.addTab(tab)
        }
        binding.sourcesTab.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                loadNews(source.id)
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                val source = tab?.tag as Source
                loadNews(source.id)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })
        binding.sourcesTab.getTabAt(0)?.select()
    }

    private fun bindNewsList(news: List<News?>?) {
        showSuccess()
        if (news.isNullOrEmpty()) {
            binding.emptyView.isVisible = true
        } else {
            binding.emptyView.isVisible = false
        }
        Constants.showBlur(binding.newsRecyclerView)
        adapter.setNews(news)
    }

    private fun showLoading() {
        Constants.blurView(binding.newsRecyclerView,20f)
        binding.loadingView.isVisible = true
        binding.errorView.isVisible = false
        binding.emptyView.isVisible = false
    }

    private fun showError(errorMessage: String, onTryAgainClick: () -> Unit) {
        Constants.blurView(binding.newsRecyclerView,20f)
        binding.errorView.isVisible = true
        binding.loadingView.isVisible = false
        binding.emptyView.isVisible = false
        binding.errorMessage.text = errorMessage
        binding.tryAgainButton.setOnClickListener({
            onTryAgainClick()
        })
    }

    private fun showSuccess() {
        binding.errorView.isVisible = false
        binding.loadingView.isVisible = false
    }

    private fun initViews() {
        Constants.showBlur(binding.newsRecyclerView)
        binding.newsRecyclerView.adapter = adapter
    }


    private fun viewArticle(article: News) {
        val articleDialog = ArticleDialogFragment(article)
        articleDialog.show(childFragmentManager, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}