package com.self.project.mediaplayer.video

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.self.project.mediaplayer.R

class VideoFilesAdapter(private val context: Context, private val listener: IVideoFilesAdapter):
    RecyclerView.Adapter<VideoFilesAdapter.VideoViewHolder>() {
    private val allFiles= ArrayList<String>()

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView = itemView.findViewById(R.id.videoName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.video_list_items, parent, false)
        val viewHolder= VideoViewHolder(view)

        view.setOnClickListener{
            listener.onItemsClicked(viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.textView.text= allFiles[position]
        holder.textView.isSelected= true
    }

    override fun getItemCount(): Int {
        return allFiles.size
    }

    fun updateList(updatedList: ArrayList<String>){
        allFiles.clear()
        allFiles.addAll(updatedList)

        notifyDataSetChanged()
    }
}

interface IVideoFilesAdapter{
    fun onItemsClicked(adapterPosition: Int)
}