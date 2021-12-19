package com.self.project.mediaplayer.image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.self.project.mediaplayer.R
import com.squareup.picasso.Picasso
import java.io.File
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class DisplayImageActivity : AppCompatActivity() {
    lateinit var displayImage: ImageView
    private lateinit var scaleGestureDetector: ScaleGestureDetector
    var mScaleFactor= 1.0f

    /*//Update for swiping
    lateinit var gdt: GestureDetector
    val MIN_SWIPPING_DISTANCE= 50
    val THRESHOLD_VELOCITY= 50*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_image)

        val imageName= intent.getStringExtra("imageName")
        val imagePath= intent.getStringExtra("imagePath")

        supportActionBar?.title= imageName

        displayImage= findViewById(R.id.displayImage)
        scaleGestureDetector= ScaleGestureDetector(this, ScaleListener())

        Picasso.get().load(File(imagePath)).placeholder(R.drawable.imageplaceholder).into(displayImage)
        //Picasso.get().isLoggingEnabled= true
        //Update for swiping
        /*gdt= GestureDetector(this, GestureListener())
        displayImage.setOnTouchListener(this)*/
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result= scaleGestureDetector.onTouchEvent(event)
        return true
    }

    inner class ScaleListener: ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector?): Boolean {
            mScaleFactor *= detector?.scaleFactor!!
            mScaleFactor = max(0.1f, min(mScaleFactor, 5.0f))

            // on below line we are setting
            // scale x and scale y to our image view.
            displayImage.scaleX = mScaleFactor
            displayImage.scaleY = mScaleFactor
            return true
        }
    }

    /*//Update for swiping
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        gdt.onTouchEvent(event)
        return true
    }

    inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            if (e1!!.x - e2!!.x > MIN_SWIPPING_DISTANCE && abs(velocityX) > THRESHOLD_VELOCITY)
            {
                Toast.makeText(applicationContext, "Swipped left side", Toast.LENGTH_SHORT).show()
                viewNextImage()
                return false
            }
            else if (e2.x - e1.x > MIN_SWIPPING_DISTANCE && abs(velocityX) > THRESHOLD_VELOCITY)
            {
                Toast.makeText(applicationContext, "Swipped right side", Toast.LENGTH_SHORT).show()
                viewPreviousImage()
                return false
            }
            return false
        }
    }

    private fun viewNextImage() {

    }

    private fun viewPreviousImage() {

    }*/
}
