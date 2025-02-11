package com.example.trendify.ui.home.fragments

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
import com.example.trendify.ui.home.NewsAdapter
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class NewsFragment : Fragment() {
    lateinit var binding: FragmentNewsBinding
    private val adapter: NewsAdapter = NewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSources()
        initViews()
    }

    private fun loadSources() {
        // loading
        showLoading()

        ApiManager.webService().getSources().enqueue(object : Callback<SourcesResponse> {
            override fun onResponse(
                call: Call<SourcesResponse>,
                response: Response<SourcesResponse>
            ) {
                if (!response.isSuccessful) {
                    val errorResponse =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    val message = errorResponse.message ?: "Something went wrong"
                    showError(message) {
                        binding.tryAgainButton.setOnClickListener({
                            loadSources()
                        })
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
        ApiManager.webService().getNews(source = sourceId ?: "abc-news")
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
        showRv()
        adapter.setNews(news)
    }

    private fun showLoading() {
        blurRv()
        binding.loadingView.isVisible = true
        binding.errorView.isVisible = false
        binding.emptyView.isVisible = false
    }

    private fun showError(errorMessage: String, onTryAgainClick: () -> Unit) {
        blurRv()
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

    private fun blurRv() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) { // API 31+
            val blurEffect = RenderEffect.createBlurEffect(20f, 20f, Shader.TileMode.CLAMP)
            binding.newsRecyclerView.setRenderEffect(blurEffect)
        }
    }

    private fun showRv() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            binding.newsRecyclerView.setRenderEffect(null)
    }

    private fun initViews() {
        showRv()
        binding.newsRecyclerView.adapter = adapter
    }


}