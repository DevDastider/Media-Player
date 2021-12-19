package com.self.project.mediaplayer.video

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import com.self.project.mediaplayer.R
import com.self.project.mediaplayer.music.MusicPlayerActivity.Companion.mediaPlayer

class YoutubePlayerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private val apiKey= "AIzaSyBn_vkKN4pu3DlX1v8Dvr4I1KoAJX5oRNA"                                       //AIzaSyDI4YSiwHQEuFkFUIADjtFS1oDBzaCkTj0
    private lateinit var videoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_youtube_player)

        try{
            if (mediaPlayer.isPlaying){
                //Log.e("Error", "try te dhukeche")
                mediaPlayer.stop()
            }
        }
        catch (e: Exception){
            Log.e("Error", "${e.printStackTrace()}")
        }

        val ytPlayer: YouTubePlayerView= findViewById(R.id.ytPlayer)

        videoId= intent.getStringExtra("id").toString()

        ytPlayer.initialize(apiKey, this)
    }

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) {
        p1?.loadVideo(videoId)
        p1?.play()
    }

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Toast.makeText(this, "Video not found", Toast.LENGTH_SHORT).show()
    }

    //To enable full screen-mode for videos
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (
                // Set the content to appear under the system bars so that the
                // content doesn't resize when the system bars hide and show.
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}