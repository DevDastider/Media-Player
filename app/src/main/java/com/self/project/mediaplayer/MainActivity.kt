package com.self.project.mediaplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.self.project.mediaplayer.image.ImageMenuActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun openMediaPlayer(view: View) {
        val intent= Intent(this, MediaPlayerActivity::class.java)
        startActivity(intent)
    }

    fun openImages(view: View) {
        val intent= Intent(this, ImageMenuActivity::class.java)
        startActivity(intent)
    }
}