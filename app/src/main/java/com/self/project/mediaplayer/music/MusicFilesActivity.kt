package com.self.project.mediaplayer.music

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.self.project.mediaplayer.R
import com.self.project.mediaplayer.music.MusicPlayerActivity.Companion.position
import java.io.File

class MusicFilesActivity : AppCompatActivity(), IMusicFilesAdapter, MultiplePermissionsListener {
    private lateinit var recyclerView: RecyclerView
    private val items= arrayListOf<String>()
    private lateinit var adapter: MusicFilesAdapter
    lateinit var mySongs: ArrayList<File>
    private val debugTag= "DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_files)
        recyclerView= findViewById(R.id.recyclerView)
        recyclerView.layoutManager= LinearLayoutManager(this)
        adapter= MusicFilesAdapter(this, this)
        recyclerView.adapter= adapter
        runtimePermission()
    }

    fun runtimePermission(){
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO).withListener(this).check()
    }

    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
        displaySongs()
    }

    override fun onPermissionRationaleShouldBeShown(
        p0: MutableList<PermissionRequest>?,
        p1: PermissionToken?
    ) {
        p1?.continuePermissionRequest()
    }

    private fun findSongs(file: File): ArrayList<File>{
        val arrList= ArrayList<File>()
        val files: Array<File>? = file.listFiles()

        if (files != null) {
            for (singleFile in files){
                if (singleFile.isDirectory && !singleFile.isHidden){
                    Log.d(debugTag, "Directory found")
                    arrList.addAll(findSongs(singleFile))
                }
                else{
                    Log.d(debugTag, "file found "+ file.name.toString())
                    if (singleFile.name.endsWith(".mp3") || singleFile.name.endsWith(".wav")){
                        Log.d(debugTag, "mp3 file found")
                        arrList.add(singleFile)
                    }
                }
            }
        }
        else{
            Log.d(debugTag, "khuje pache na")
            //Toast.makeText(this, "No files found", Toast.LENGTH_LONG).show()
        }
        return arrList
    }

    private fun displaySongs(){
        mySongs = findSongs(Environment.getExternalStorageDirectory())

            for (i in 0 until mySongs.size) {
                val value= mySongs[i].name.toString().replace(".mp3", "")
                items.add(value)
                Log.d(debugTag, "Finally")
            }

        adapter.updateList(items)
        /*val filesArray= ArrayList<String>()
        filesArray.addAll(arrayOf("abc", "bcd", "def", "ghidcjoaisdoiasmdkasmdlasmdklasmdkaslmdakslmd", "abc", "bcd", "def", "ghi"))
        adapter.updateList(filesArray)*/
    }

    override fun onItemsClicked(songName: String, adapterPosition: Int) {
        //Toast.makeText(this, "Clicked: $value", Toast.LENGTH_LONG).show()
        startActivity(Intent(this@MusicFilesActivity, MusicPlayerActivity::class.java).apply {
            putExtra("songName", songName)
            putExtra("songs", mySongs)
            putExtra("position", adapterPosition)
            if (adapterPosition==position)
                putExtra("flag", true)
            else
                putExtra("flag", false)
        })
    }
}