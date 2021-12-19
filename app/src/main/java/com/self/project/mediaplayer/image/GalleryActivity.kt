package com.self.project.mediaplayer.image

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.self.project.mediaplayer.R

class GalleryActivity : AppCompatActivity(), IGalleryViewAdapter, PermissionListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryViewAdapter
    private val imageFiles= arrayListOf<Image>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        supportActionBar?.title= "Gallery"
        recyclerView= findViewById(R.id.galleryView)
        adapter= GalleryViewAdapter(this, this)
        Log.e("adapter", "$adapter created")
        recyclerView.adapter= adapter
        recyclerView.layoutManager= GridLayoutManager(this, 4)
        runtimePermission()
    }

    private fun runtimePermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(this).check()
    }

    override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
        getImageFiles()
    }

    override fun onPermissionDenied(p0: PermissionDeniedResponse?) {

    }

    override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
        p1?.continuePermissionRequest()
    }

    private fun getImageFiles() {
        val uri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection= arrayOf(MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME)
        var cursor= contentResolver.query(uri, projection, null, null, null)

        try {
            cursor!!.moveToFirst()
            do {
                val image= Image()
                image.imagePath= cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                image.imageName= cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                imageFiles.add(image)
            }while (cursor.moveToNext())

            cursor.close()
        }
        catch (e: Exception){
            Toast.makeText(this, "No images found", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        Log.d("adapter","Updating List")
        Log.d("adapter","${imageFiles[0].imagePath}")
        Log.e("adapter", "$adapter")
        adapter.updateList(imageFiles)
    }

    override fun onImageClicked(position: Int) {
        //Toast.makeText(this, "$position clicked", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, DisplayImageActivity::class.java).apply {
            putExtra("imageName", imageFiles[position].imageName)
            putExtra("imagePath", imageFiles[position].imagePath)
        })
    }
}