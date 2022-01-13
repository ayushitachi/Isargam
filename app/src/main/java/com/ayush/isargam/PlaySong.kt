package com.ayush.isargam

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.lang.Exception

class PlaySong : AppCompatActivity() {
    var music = MediaPlayer()
    var updateSeek = Thread()

    override fun onDestroy() {
        super.onDestroy()
        music.pause()
        music.release()
        updateSeek.interrupt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_song)

        val Sname = findViewById<TextView>(R.id.Sname)
        val playPause = findViewById<ImageView>(R.id.play)
        val next = findViewById<ImageView>(R.id.forward)
        val back = findViewById<ImageView>(R.id.backward)
        val seekBar = findViewById<SeekBar>(R.id.seekBar)

        val intent = intent
        val bundle = intent.extras

        Sname.text = bundle?.get("SongName") as CharSequence?

        val songsList = bundle?.get("SongList") as ArrayList<File>
        var position = bundle.getInt("position")
        val currSong = songsList[position]

        val uri = Uri.parse(currSong.toString())
        music = MediaPlayer.create(this, uri)
        seekBar.max = music.duration

        updateSeek = object : Thread() {
            override fun run() {
                var p = 0 ;
                try {
                    while (p < music.duration){
                        p = music.currentPosition
                        seekBar.progress = p
                        sleep(500)
                    }

                } catch (e : Exception){
                    e.printStackTrace()
                }
            }
        }
        updateSeek.start()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (seekBar != null) {
                    music.seekTo(seekBar.progress)
                }
            }

        })


        playPause.setOnClickListener {
            if (music.isPlaying){
                music.pause()
                playPause.setImageResource(R.drawable.play)
            } else{
                music.start()
                playPause.setImageResource(R.drawable.pause)
            }
        }

        next.setOnClickListener {
            music.pause()
            music.release()
            updateSeek.interrupt()
            seekBar.progress = 0
            playPause.setImageResource(R.drawable.play)

            if (position != songsList.size - 1){
                position += 1;
            } else{
                position = 0 ;
            }
            val nextSong = songsList[position]
            Sname.text = nextSong.name.replace(".mp", "")

            val nextUri = Uri.parse(nextSong.toString())
            music = MediaPlayer.create(this, nextUri)

            updateSeek = object : Thread() {
                override fun run() {
                    var p = 0 ;
                    try {
                        while (p < music.duration){
                            p = music.currentPosition
                            seekBar.progress = p
                            sleep(800)
                        }
                    } catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }
            updateSeek.start()

        }

        back.setOnClickListener {
            music.pause()
            music.release()
            updateSeek.interrupt()
            seekBar.progress = 0
            playPause.setImageResource(R.drawable.play)

            if (position == 0){
                position = songsList.size - 1;
            } else{
                position -= 1
            }
            val backSong = songsList[position]
            Sname.text = backSong.name.replace(".mp", "")

            val backUri = Uri.parse(backSong.toString())
            music = MediaPlayer.create(this, backUri)

            updateSeek = object : Thread() {
                override fun run() {
                    var p = 0 ;
                    try {
                        while (p < music.duration){
                            p = music.currentPosition
                            seekBar.progress = p
                            sleep(800)
                        }
                    } catch (e : Exception){
                        e.printStackTrace()
                    }
                }
            }
            updateSeek.start()

        }



    }
}

