package com.example.trendify.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.trendify.R
import com.example.trendify.api.model.Category
import com.example.trendify.databinding.CategoryItemLeftBinding
import com.example.trendify.databinding.CategoryItemRightBinding

class CategoriesAdapter(
    private val categories: List<Category> = Category.getCategories(),
    val onCategoryClick: (category: Category) -> Unit
) :
    RecyclerView.Adapter<CategoriesAdapter.BaseViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 1) R.layout.category_item_right else R.layout.category_item_left
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            R.layout.category_item_left -> {
                val binding = CategoryItemLeftBinding.inflate(layoutInflater, parent, false)
                LeftViewHolder(binding)
            }

            else -> {
                val binding = CategoryItemRightBinding.inflate(layoutInflater, parent, false)
                RightViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.bind(categories[position])
        holder.itemView.setOnClickListener({
            onCategoryClick(categories[position])
        })
    }

    override fun getItemCount(): Int = categories.size

    sealed class BaseViewHolder(binding: ViewGroup) : RecyclerView.ViewHolder(binding) {
        abstract fun bind(category: Category)
    }

    class LeftViewHolder(private val binding: CategoryItemLeftBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(category: Category) {
            binding.categoryName.text = binding.root.context.getString(category.title)
            binding.categoryImage.setImageResource(category.imgRes)
        }
    }

    class RightViewHolder(private val binding: CategoryItemRightBinding) :
        BaseViewHolder(binding.root) {
        override fun bind(category: Category) {
            binding.categoryName.text = binding.root.context.getString(category.title)
            binding.categoryImage.setImageResource(category.imgRes)
        }
    }
}
