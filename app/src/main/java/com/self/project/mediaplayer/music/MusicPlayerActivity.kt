package com.self.project.mediaplayer.music

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.res.Configuration
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.gauravk.audiovisualizer.visualizer.BarVisualizer
import com.self.project.mediaplayer.R
import java.io.File

class MusicPlayerActivity : AppCompatActivity(), OnSeekBarChangeListener, Runnable {

    private lateinit var playPauseButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var fastFwdButton: Button
    private lateinit var fastBwdButton: Button
    private lateinit var txtSongName: TextView
    private lateinit var txtSongStart: TextView
    private lateinit var txtSongEnd: TextView
    private lateinit var seekBar: SeekBar
    private lateinit var barVisualizer: BarVisualizer
    private lateinit var imageView: ImageView
    private lateinit var songName: String
    private lateinit var mySongs: ArrayList<File>
    private lateinit var updateSeekBar: Thread
    private val delay: Long = 1000

    companion object {
        val handler= Handler()
        var mediaPlayer: MediaPlayer = MediaPlayer()
        var position= -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)

        bindingViews()

        if (intent.getBooleanExtra("flag", false)){
            playPauseButton.setBackgroundResource(R.drawable.ic_pause)
            resumeActivity()
        }
        else {
            //To check whether a song is already playing or not
            if (mediaPlayer != null) {
                mediaPlayer.start()
                mediaPlayer.release()
            }

            position = intent.getIntExtra("position", 0)

            fetchList()
            playSong(position)
            controllingMediaInterface()
        }
    }

    private fun bindingViews(){
        playPauseButton = findViewById(R.id.playPauseBttn)
        nextButton = findViewById(R.id.nextBttn)
        previousButton = findViewById(R.id.previousBttn)
        fastFwdButton = findViewById(R.id.fastFwdBttn)
        fastBwdButton = findViewById(R.id.fastBwdBttn)

        txtSongName = findViewById(R.id.txtSong)
        txtSongStart = findViewById(R.id.startSong)
        txtSongEnd = findViewById(R.id.endSong)

        seekBar = findViewById(R.id.seekBar)
        barVisualizer = findViewById(R.id.wave)

        imageView = findViewById(R.id.imgSong)
    }

    private fun fetchList() {
        //Song list from previous activity
        mySongs = ArrayList(intent.extras?.getParcelableArrayList("songs"))
    }

    private fun controllingMediaInterface() {
        //Setting up name
        txtSongName.text = songName.replace(".mp3", "")
        //Showing current time in textview
        handler.postDelayed(this, delay)

        //Play pause function
        playPauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                playPauseButton.setBackgroundResource(R.drawable.ic_play)
                mediaPlayer.pause()
            } else {
                playPauseButton.setBackgroundResource(R.drawable.ic_pause)
                mediaPlayer.start()
            }
        }

        seekBar.setOnSeekBarChangeListener(this)

        nextButton.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.release()
            position = (position + 1) % mySongs.size
            playSong(position)
            seekBarFunction()
            playPauseButton.setBackgroundResource(R.drawable.ic_pause)
            //edit
           // setUpBarVisualizer(mediaPlayer.audioSessionId)
            startAnimation(imageView, 360f)
        }

        previousButton.setOnClickListener {
            mediaPlayer.stop()
            mediaPlayer.release()
            position = if ((position - 1) < 0) {
                mySongs.size - 1
            } else {
                position - 1
            }

            playSong(position)
            seekBarFunction()
            playPauseButton.setBackgroundResource(R.drawable.ic_pause)
            startAnimation(imageView, -360f)
            //edit
            //setUpBarVisualizer(mediaPlayer.audioSessionId)
        }

        fastFwdButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.seekTo(mediaPlayer.currentPosition + 10000)
            }
        }

        fastBwdButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.seekTo(mediaPlayer.currentPosition - 10000)
            }
        }

        seekBarFunction()
    }

    private fun playSong(pos: Int) {
        //Setting up song name in player
        songName = mySongs[pos].name
        mediaPlayer.release()

        txtSongName.text = songName.replace(".mp3", "")
        //Parsing mp3 file through uri
        val uri = Uri.parse(mySongs[pos].toString())
        mediaPlayer = MediaPlayer.create(this, uri)
        mediaPlayer.start()

        //Log.e("audioId", "${mediaPlayer.audioSessionId}")
        setUpBarVisualizer(mediaPlayer.audioSessionId)

        //Setting up on completing music
        mediaPlayer.setOnCompletionListener {
            nextButton.performClick()
        }

        //Showing end time in textview
        val endTime = createTime(mediaPlayer.duration)
        txtSongEnd.text = endTime
    }

    private fun setUpBarVisualizer(id: Int) {
        //Setting up bar visualizer
        if (id != -1) {
            barVisualizer.setAudioSessionId(id)
        }
    }

    private fun startAnimation(view: View, degree: Float) {
        val objectAnimator = ObjectAnimator.ofFloat(view, "rotation", 0f, degree)
        objectAnimator.duration = 1000
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(objectAnimator)
        animatorSet.start()
    }

    private fun createTime(duration: Int): String {
        var time = ""
        val min = duration / 1000 / 60
        val sec = duration / 1000 % 60

        time = "$min:"
        if (sec < 10) {
            time += "0"
        }
        time += sec

        return time
    }

    private fun seekBarFunction() {
        updateSeekBar = Thread {
            val totalDuration = mediaPlayer.duration
            var currentPosition = 0

            while (currentPosition < totalDuration) {
                try {
                    Thread.sleep(500)
                    currentPosition = mediaPlayer.currentPosition
                    seekBar.progress = currentPosition
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        seekBar.max = mediaPlayer.duration
        updateSeekBar.start()
        seekBar.progressDrawable.setColorFilter(resources.getColor(R.color.black), PorterDuff.Mode.MULTIPLY)
    }

    //Overriding Seek bar functions
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        seekBar?.progress?.let { mediaPlayer.seekTo(it) }
    }

    //Overriding Runnable run() for showing current position
    override fun run() {
        val currentTime = createTime(mediaPlayer.currentPosition)
        txtSongStart.text = currentTime
        handler.postDelayed(this, delay)
    }


    private fun resumeActivity() {
        val endTime = createTime(mediaPlayer.duration)
        txtSongEnd.text = endTime
        val current= mediaPlayer.currentPosition
        fetchList()
        playSong(position)
        //Seeking the player to current position
        mediaPlayer.seekTo(current)
        controllingMediaInterface()
        seekBarFunction()
        //setUpBarVisualizer(mediaPlayer.audioSessionId)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        setContentView(R.layout.activity_music_player)

        bindingViews()

        if (mediaPlayer.isPlaying){
            playPauseButton.setBackgroundResource(R.drawable.ic_pause)
            resumeActivity()
        }
        else{
            playPauseButton.setBackgroundResource(R.drawable.ic_play)
            resumeActivity()
            mediaPlayer.pause()
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (barVisualizer != null) {
            barVisualizer.release()
        }
    }
}