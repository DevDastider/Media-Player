package com.self.project.mediaplayer.video

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.MediaController
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import com.self.project.mediaplayer.R
import com.self.project.mediaplayer.music.MusicPlayerActivity.Companion.mediaPlayer
import java.io.File

class VideoPlayerActivity : AppCompatActivity() {
    private var position: Int= -1
    private lateinit var myVideos: ArrayList<File>
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        try{
            if (mediaPlayer.isPlaying){
                //Log.e("Error", "try te dhukeche")
                mediaPlayer.stop()
            }
        }catch (e: Exception){
            Log.e("Error", "${e.printStackTrace()}")
        }

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)

        position= intent.getIntExtra("position", -1)
        myVideos= ArrayList(intent.extras?.getParcelableArrayList("videoList"))

        videoView= findViewById(R.id.videoView)

        playVideo(myVideos[position].path)


        /*videoView.setOnCompletionListener {
            position= (position + 1) % myVideos.size
            playVideo(myVideos[position].path)
        }*/
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

    private fun playVideo(path: String){
        if (path!=null){
            videoView.setVideoPath(path)
            val videoController= MediaController(this)

            videoView.setOnPreparedListener {
                videoView.start()
                videoView.setMediaController(videoController)
            }

            videoController.setPrevNextListeners({
                //For Next Video
                position= (position + 1) % 4//myVideos.size     //Hard-Coded for YT
                playVideo(myVideos[position].path)
            },{
                //For Previous Video
                position= if ((position - 1) < 0) {
                    /*myVideos.size*/4 - 1          //Hard-Coded for YT
                } else {
                    position - 1
                }
                playVideo((myVideos[position].path))
            })
        }
        else{
            Toast.makeText(this, "Video path doesn't exist", Toast.LENGTH_LONG).show()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}