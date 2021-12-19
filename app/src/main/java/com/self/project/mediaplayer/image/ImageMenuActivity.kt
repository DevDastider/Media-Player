package com.self.project.mediaplayer.image

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.self.project.mediaplayer.R
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ImageMenuActivity : AppCompatActivity(), MultiplePermissionsListener {
    private val SAVE_IMAGE_REQUEST_CODE: Int= 228
    private lateinit var photoPath: String
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_menu)
        supportActionBar?.title= "Images"

        imageView= findViewById(R.id.capturedImage)
    }

    fun viewGallery(view: View) {
        val intent= Intent(this, GalleryActivity::class.java)
        startActivity(intent)
    }

    fun startCamera(view: View) {
        Dexter.withContext(this).withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(this).check()
    }

    override fun onPermissionsChecked(p0: MultiplePermissionsReport?) {
        takePhoto()
    }

    override fun onPermissionRationaleShouldBeShown(p0: MutableList<PermissionRequest>?, p1: PermissionToken?) {
        p1?.continuePermissionRequest()
    }

    private fun takePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                takePhotoIntent -> takePhotoIntent.resolveActivity(this.packageManager)
            if (takePhotoIntent == null){
                Toast.makeText(this, "Unable to save photo", Toast.LENGTH_SHORT).show()
            } else{
                // If we have valid intent
                val photoFile: File= createImageFile()
                photoFile.also {
                    val photoUri: Uri= FileProvider.getUriForFile(this, "com.self.project.mediaplayer.fileprovider", it)
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    startActivityForResult(takePhotoIntent, SAVE_IMAGE_REQUEST_CODE)
                }
            }
        }
    }

    private fun createImageFile(): File {
        //Generating timestamp to use it in file name
        val timestamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        //Getting access to the directory where we can write our images
        val storageDir: File? = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        return File.createTempFile("IMAGE_${timestamp}", ".jpg", storageDir).apply {
            photoPath= absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK){
            if (requestCode == SAVE_IMAGE_REQUEST_CODE){
                Toast.makeText(this, "Image saved", Toast.LENGTH_LONG).show()
                Picasso.get().load(File(photoPath)).into(imageView)
                galleryAddPic()
            }
        }
        else{
            File(photoPath).delete()
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(photoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    override fun onStop() {
        super.onStop()
        if(!isChangingConfigurations) {
            imageView.setImageDrawable(null)
        }
    }
}