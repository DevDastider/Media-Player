package com.self.project.mediaplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.self.project.mediaplayer.music.MusicFilesActivity
import com.self.project.mediaplayer.video.VideoFilesActivity

class MediaPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_player)
    }

    fun browseMusic(view: View) {
        val intent= Intent(this, MusicFilesActivity::class.java)
        startActivity(intent)
    }

    fun browseVideo(view: View) {
        val intent= Intent(this, VideoFilesActivity::class.java)
        startActivity(intent)
    }
}