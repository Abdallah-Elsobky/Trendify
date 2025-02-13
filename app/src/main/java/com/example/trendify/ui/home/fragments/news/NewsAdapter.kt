package com.example.trendify.ui.home.fragments.news

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.trendify.R
import com.example.trendify.api.model.newsResponse.News
import com.example.trendify.databinding.NewsItemBinding
import com.github.marlonlom.utilities.timeago.TimeAgo

class NewsAdapter(val onNewsClick: (article: News) -> Unit) :
    Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList: List<News?>? = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val binding = NewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewsViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val news = newsList?.get(position)
        holder.binding.root.setOnClickListener {
            onNewsClick(news!!)
        }
        holder.bind(news!!)
    }

    override fun getItemCount(): Int = newsList!!.size

    inner class NewsViewHolder(val binding: NewsItemBinding) : ViewHolder(binding.root) {
        fun bind(newsItem: News) {
            binding.newsTitle.text = newsItem.title
            binding.author.text = newsItem.author
            binding.time.text = TimeAgo.using(newsItem.dateToMilliseconds())
            binding.imageLoading.isVisible = true
            Glide.with(binding.root)
                .load(newsItem.urlToImage)
                .error(R.drawable.error_loading)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageLoading.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.imageLoading.isVisible = false
                        return false
                    }

                })
                .into(binding.newsImg)
        }
    }

    fun setNews(news: List<News?>?) {
        newsList = news
        notifyDataSetChanged()
    }
}