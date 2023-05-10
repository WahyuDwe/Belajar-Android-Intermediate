package com.dwi.dicodingstoryapp.ui.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.dwi.dicodingstoryapp.data.source.remote.response.StoryResult
import com.dwi.dicodingstoryapp.databinding.ItemStoryBinding
import com.dwi.dicodingstoryapp.ui.detail.DetailActivity
import com.dwi.dicodingstoryapp.utils.Constanta.STORIES_ID
import com.dwi.dicodingstoryapp.utils.DiffUtils

class MainAdapter : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {
    private var oldList = emptyList<StoryResult>()

    inner class MainViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: StoryResult) {
            with(binding) {
                ivItemStoriesImage.load(stories.photoUrl)
                tvName.text = stories.name
                tvDescription.text = stories.description
                tvDate.text = stories.createdAt
            }

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra(STORIES_ID, stories.id)
                val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    itemView.context as Activity,
                    Pair(binding.ivItemStoriesImage, "image"),
                    Pair(binding.tvName, "name"),
                    Pair(binding.tvDescription, "description"),
                )
                itemView.context.startActivity(intent, optionsCompat.toBundle())
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemStoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val oldPosition = oldList[position]
        holder.bind(oldPosition)
    }

    override fun getItemCount(): Int = oldList.size

    fun setData(newList: ArrayList<StoryResult>) {
        val diffUtils = DiffUtils(oldList, newList)
        val diffResult = DiffUtil.calculateDiff(diffUtils)
        oldList = newList
        diffResult.dispatchUpdatesTo(this@MainAdapter)
    }
}