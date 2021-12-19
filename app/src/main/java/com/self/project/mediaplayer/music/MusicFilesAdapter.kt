package com.self.project.mediaplayer.music

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.self.project.mediaplayer.R

class MusicFilesAdapter(private val context: Context, private val listener: IMusicFilesAdapter): RecyclerView.Adapter<MusicFilesAdapter.MusicViewHolder>() {
    private val allFiles= ArrayList<String>()

    inner class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val textView: TextView= itemView.findViewById(R.id.musicName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        val view= LayoutInflater.from(context).inflate(R.layout.music_list_items, parent, false)
        val viewHolder= MusicViewHolder(view)

        view.setOnClickListener{
            listener.onItemsClicked(allFiles[viewHolder.adapterPosition], viewHolder.adapterPosition)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        Log.e("adapter", "Music")
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

interface IMusicFilesAdapter{
    fun onItemsClicked(songName: String, adapterPosition: Int)
}