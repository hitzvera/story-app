package com.hitzvera.storyapp.ui.stories
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hitzvera.storyapp.databinding.ItemStoryUserBinding
import com.hitzvera.storyapp.model.ListStoryItem

class StoriesAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<StoriesAdapter.MyViewHolder>() {

    private var oldStoryItem = emptyList<ListStoryItem>()

    class MyViewHolder(private val binding: ItemStoryUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(listStoryItem: ListStoryItem){
            Glide.with(itemView)
                .load(listStoryItem.photoUrl)
                .into(binding.imgProfile)

            binding.profileName.text = listStoryItem.name
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(ItemStoryUserBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(oldStoryItem[position])
        holder.itemView.setOnClickListener {
            onClickListener.onClick(oldStoryItem[position])
        }
    }

    override fun getItemCount(): Int = oldStoryItem.size

    fun setData(newStoryItem: List<ListStoryItem>) {
        val diffUtil = StoriesDiffUtil(oldStoryItem, newStoryItem)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        oldStoryItem = newStoryItem
        diffResult.dispatchUpdatesTo(this)
    }
    class OnClickListener(val clickListener: (listStoryItem: ListStoryItem) -> Unit) {
        fun onClick(listStoryItem: ListStoryItem) = clickListener(listStoryItem)
    }
}
