package com.ahmed.weatherapptask.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.ahmed.weatherapptask.R
import com.ahmed.weatherapptask.RoomDB.Models_DB.ImageEntity
import com.ahmed.weatherapptask.databinding.CardImageBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class SavedImagesAdapter(
    private val context: Context,
    private val savedImages: List<ImageEntity>,
    private val onItemClickListener: OnItemClickListener,
) :
    RecyclerView.Adapter<SavedImagesAdapter.SavedImagesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedImagesViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = CardImageBinding.inflate(inflater)
        return SavedImagesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedImagesViewHolder, position: Int) =
        holder.bind(savedImages[position])


    override fun getItemCount(): Int {
        return savedImages.size
    }

    inner class SavedImagesViewHolder(val binding: CardImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: ImageEntity) {
            with(binding) {
                Glide.with(context)
                    .load(image.image_path)
                    .into(imageThumbnail)

                //Handle on item Clicked
                imageContainer.setOnClickListener {
                    if (adapterPosition != NO_POSITION) {
                        onItemClickListener.onItemClick(adapterPosition, image)
                    }
                }
            }

        }
    }


    /**
     * Handle on item Clicked
     * */
    interface OnItemClickListener {
        fun onItemClick(position: Int, image: ImageEntity)
    }


}