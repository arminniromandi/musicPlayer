package ir.arminapp.musicplayer.activity

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import ir.arminapp.musicplayer.databinding.ActivityMusicBinding
import ir.arminapp.musicplayer.ext.MusicHelper
import ir.arminapp.musicplayer.recyclerView.model.MusicFile


class MusicActivity : AppCompatActivity() {
    private val musicHelper = MusicHelper(this)
    private val musicList by lazy { musicHelper.getAllMusic() }
    private var mediaPlayer: MediaPlayer? = null
    private var currentMusicId: Int = -1
    private var isPlaying = false
    private lateinit var binding: ActivityMusicBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicBinding.inflate(layoutInflater)
        setContentView(binding.root)

        currentMusicId = intent.getIntExtra("id", -1) - 1
        playMusic(id = currentMusicId)
        binding.apply {
            nextBtn.setOnClickListener { playNextMusic() }
            perBtn.setOnClickListener { playPreviousMusic() }
            playBtn.setOnClickListener { togglePlayPause() }
        }
        binding.back.setOnClickListener { finish() }

        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(p0: SeekBar?, p1: Int, fromUser: Boolean) {

                    if (fromUser)
                        mediaPlayer?.seekTo(p1)
                }

                override fun onStartTrackingTouch(p0: SeekBar?) {

                }

                override fun onStopTrackingTouch(p0: SeekBar?) {

                }


            }
        )
        initSeekBar()


    }

    private fun playMusic(id: Int) {
        if (id != currentMusicId) {
            stopMusic()
            currentMusicId = id
            val musicFile = musicList.getOrNull(id) ?: return
            startMusic(musicFile)
            setDataResource(musicFile)
            initSeekBar()
        } else {
            togglePlayPause()
        }
    }

    private fun initSeekBar() {
        binding.seekBar.max = mediaPlayer?.duration ?: 100
        val handler = Handler(mainLooper)
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    binding.seekBar.progress = mediaPlayer!!.currentPosition
                } catch (e: Exception) {
                    binding.seekBar.progress = 0

                }
                handler.postDelayed(this, 1000)
            }

        }, 0)
    }

    private fun togglePlayPause() {
        if (isPlaying) {
            pauseMusic()
        } else {
            if (mediaPlayer == null) {
                startMusic(musicList.getOrNull(currentMusicId) ?: return)
                setDataResource(musicList.getOrNull(currentMusicId) ?: return)
            } else {
                mediaPlayer?.start()
                isPlaying = true
            }
        }
    }


    private fun startMusic(musicFile: MusicFile) {
        isPlaying = true
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(musicFile.path)
        mediaPlayer?.prepareAsync()
        mediaPlayer?.setOnPreparedListener {
            it.start()
            initSeekBar()
            Log.i("test", "test1")
        }


    }

    private fun pauseMusic() {
        isPlaying = false
        mediaPlayer?.pause()
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun setDataResource(musicFile: MusicFile) {
        //set title
        binding.songText.text = musicFile.title


    }

    private fun playNextMusic() {
        val nextId = currentMusicId + 1
        playMusic(nextId)

    }

    private fun playPreviousMusic() {
        val previousId = currentMusicId - 1
        playMusic(previousId)


    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
    }
}
