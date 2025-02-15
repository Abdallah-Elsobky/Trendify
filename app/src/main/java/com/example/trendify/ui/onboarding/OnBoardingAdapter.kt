package com.example.trendify.ui.onboarding

import Constants
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.trendify.databinding.BoardItemBinding

class OnboardingAdapter(private val items: List<BoardItem>) :
    Adapter<OnboardingAdapter.BoardViewHolder>() {


    inner class BoardViewHolder(val binding: BoardItemBinding) : ViewHolder(binding.root) {
        fun bind(item: BoardItem) {
            binding.lottieBoard.setAnimation(item.image)
            binding.titleOnboarding.text = item.title
            binding.descriptionOnboarding.text = item.description
            binding.lottieBoard2.setAnimation(item.image)
            Constants.blurView(binding.lottieBoard2,200f)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardViewHolder {
        val binding = BoardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BoardViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BoardViewHolder, position: Int) {
        val board = items[position]
        holder.bind(board)
    }
}
