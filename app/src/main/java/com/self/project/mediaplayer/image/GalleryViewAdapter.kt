package com.self.project.mediaplayer.image

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.self.project.mediaplayer.R
import com.squareup.picasso.Picasso
import java.io.File

class GalleryViewAdapter(val context: Context, private val listener: IGalleryViewAdapter): RecyclerView.Adapter<GalleryViewAdapter.GalleryViewHolder>() {
    private val imageFiles= ArrayList<Image>()

    inner class GalleryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView= itemView.findViewById(R.id.thumbnail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.gallery_item, parent, false)
        val viewHolder= GalleryViewHolder(view)

        view.setOnClickListener{
            listener.onImageClicked(viewHolder.adapterPosition)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val currentImage=imageFiles[position]
        Log.e("adapter", currentImage.imagePath!!)
        Picasso.get().load(File(currentImage.imagePath!!)).placeholder(R.drawable.imageplaceholder).into(holder.imageView)
        //Picasso.get().load(File(imageFiles[position])).placeholder(R.drawable.imageplaceholder).into(holder.imageView)
        //Picasso.get().isLoggingEnabled= true
    }

    override fun getItemCount(): Int {
        return imageFiles.size
    }

    fun updateList(updatedList: ArrayList<Image>){
        imageFiles.clear()
        imageFiles.addAll(updatedList)

        notifyDataSetChanged()
    }
}

interface IGalleryViewAdapter{
    fun onImageClicked(position: Int)
}