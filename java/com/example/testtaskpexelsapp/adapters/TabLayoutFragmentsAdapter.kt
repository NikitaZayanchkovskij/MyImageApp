package com.example.testtaskpexelsapp.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testtaskpexelsapp.MainActivity
import com.example.testtaskpexelsapp.R
import com.example.testtaskpexelsapp.databinding.TabLayoutImagesListItemBinding
import com.example.testtaskpexelsapp.interfaces.IImagePressedListener
import com.example.testtaskpexelsapp.mvvm.model.ImageResourceFromPexels


class TabLayoutFragmentsAdapter(
    private val imagePressedListener: IImagePressedListener,
    private val context: MainActivity):
    ListAdapter<ImageResourceFromPexels, TabLayoutFragmentsAdapter.ImageHolder>(AllImagesComparator()) {


    class ImageHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = TabLayoutImagesListItemBinding.bind(view)


        fun setData(image: ImageResourceFromPexels,
            imagePressedListener: IImagePressedListener, context: Context) = with(binding) {

            Glide
                .with(context)
                .load(image.src.original)
                .placeholder(R.drawable.ic_image_placeholder)
                .into(binding.imTestItem)

            /* Слушатель нажатий на изображение. */
            itemView.setOnClickListener {
                imagePressedListener.imagePressed(image)
            }
        }
    }


    class AllImagesComparator: DiffUtil.ItemCallback<ImageResourceFromPexels>() {

        override fun areItemsTheSame(oldItem: ImageResourceFromPexels, newItem: ImageResourceFromPexels): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ImageResourceFromPexels, newItem: ImageResourceFromPexels): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.tab_layout_images_list_item, parent, false)

        return ImageHolder(view)
    }


    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.setData(getItem(position), imagePressedListener, context)
    }


}