package com.self.project.mediaplayer.video

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.self.project.mediaplayer.R
import java.io.File

class VideoFilesActivity : AppCompatActivity(), PermissionListener, IVideoFilesAdapter {
    private lateinit var recyclerView: RecyclerView
    private val items= arrayListOf<String>()
    private lateinit var adapter: VideoFilesAdapter
    private lateinit var myVideos: ArrayList<File>
    private val debugTag= "DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_files)
        recyclerView= findViewById(R.id.recyclerView2)
        recyclerView.layoutManager= LinearLayoutManager(this)
        adapter= VideoFilesAdapter(this, this)
        recyclerView.adapter= adapter
        runtimePermission()
    }

    private fun runtimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(this).check()
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        displayVideos()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
        TODO("Not yet implemented")
    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        p1?.continuePermissionRequest()
    }

    private fun findVideos(file: File): ArrayList<File>{
        val arrList= ArrayList<File>()
        val files: Array<File>? = file.listFiles()

        if (files != null) {
            for (singleFile in files){
                if (singleFile.isDirectory && !singleFile.isHidden){
                    Log.d(debugTag, "Directory found")
                    arrList.addAll(findVideos(singleFile))
                }
                else{
                    Log.d(debugTag, "file found "+ file.name.toString())
                    if (singleFile.name.endsWith(".mp4") || singleFile.name.endsWith(".mov")){
                        Log.d(debugTag, "video file found")
                        arrList.add(singleFile)
                    }
                }
            }
        }
        else{
            Log.d(debugTag, "khuje pache na")
        }
        return arrList
    }

    private fun displayVideos() {
        myVideos = findVideos(Environment.getExternalStorageDirectory())

        for (i in 0 until myVideos.size) {
            val value= myVideos[i].name.toString().replace(".mov", "")
            items.add(value)
            Log.d(debugTag, "Finally")
        }

        adapter.updateList(items)
    }

    override fun onItemsClicked(adapterPosition: Int) {
        //Toast.makeText(this, "Clicked: $videoName", Toast.LENGTH_LONG).show()

        startActivity(Intent(this, VideoPlayerActivity::class.java).apply {
            putExtra("position", adapterPosition)
            putExtra("videoList", myVideos)
        })

    }
}