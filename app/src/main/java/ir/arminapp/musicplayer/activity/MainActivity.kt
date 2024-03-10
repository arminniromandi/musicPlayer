package ir.arminapp.musicplayer.activity

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle

import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.ImageRequest
import com.google.android.material.tabs.TabLayout
import ir.arminapp.musicplayer.R
import ir.arminapp.musicplayer.databinding.ActivityMainBinding
import ir.arminapp.musicplayer.db.DAO.LikedDAO
import ir.arminapp.musicplayer.db.DbHandler
import ir.arminapp.musicplayer.ext.MusicHelper
import ir.arminapp.musicplayer.fragments.FavoriteFragment
import ir.arminapp.musicplayer.recyclerView.adapter.AdapterMusic
import ir.arminapp.musicplayer.recyclerView.model.MusicFile
import ir.arminapp.musicplayer.utils.MusicClickData

class MainActivity : AppCompatActivity(), MusicClickData {
    private lateinit var binding: ActivityMainBinding
    private val reqFilePermission = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)





        val filePer = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        )
        if (filePer != PackageManager.PERMISSION_GRANTED)
            requestFilePermission()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                0
            )
        }


        binding.frameLayout.visibility = View.GONE


        binding.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            binding.frameLayout.visibility = View.GONE
                            binding.recyclerView.visibility = View.VISIBLE

                        }

                        1 -> {
                            binding.frameLayout.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                            replaceFragment(FavoriteFragment())
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

            }
        )


        setMusic()


    }

    private fun setMusic() {
        val musicHelper = MusicHelper(this)
        val musicList = musicHelper.getAllMusic()

        binding.recyclerView.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        var db = DbHandler(this)

        val adapter = AdapterMusic(this, musicList, LikedDAO(db), this)
        binding.recyclerView.adapter = adapter
    }

    override fun playMusic(musicFile: MusicFile) {
        super.playMusic(musicFile)
        setDataRes(musicFile)


    }


    private fun setDataRes(data: MusicFile) {
        binding.titleText.text = data.title
        binding.artistText.text = data.artist
        val request = ImageRequest.Builder(this)
            .data(data.albumArt)
            .target(
                onSuccess = { result ->
                    // تصویر با موفقیت دریافت شد، نمایش در ImageView
                    binding.SongImge.setImageDrawable(result)
                },
                onError = { error ->
                    // خطا در دریافت تصویر، می‌توانید اقدامات مناسب را انجام دهید
                    binding.SongImge.setImageResource(R.drawable.placeholder)
                }
            )
            .build()

        // اجرای ImageRequest با استفاده از ImageLoader
        val imageLoader = binding.SongImge.context.imageLoader
        imageLoader.enqueue(request)


    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, fragment)
            .commit()
    }


    private fun requestFilePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
        )
            AlertDialog.Builder(this)
                .setTitle("درخواست مجوز")
                .setMessage("برای دسترسی به فایل نیاز است مجوز را تایید کنید")
                .setCancelable(false)
                .setPositiveButton("بله") { _, _ ->
                    reqPermission()
                }
                .setNegativeButton("خیر") { dialogInterface, _ ->
                    dialogInterface.dismiss()

                }
                .create()
                .show()
        else
            reqPermission()

    }

    private fun reqPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
            reqFilePermission
        )

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        if (requestCode == reqFilePermission) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                toast("مجوز داده شد")
            } else {
                toast("مجوز رد شد")
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }
}