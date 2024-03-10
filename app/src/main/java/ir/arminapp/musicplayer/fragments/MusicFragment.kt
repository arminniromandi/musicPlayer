package ir.arminapp.musicplayer.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arminapp.musicplayer.activity.MainActivity
import ir.arminapp.musicplayer.recyclerView.adapter.AdapterMusic
import ir.arminapp.musicplayer.databinding.FragmentMusicFragmentBinding
import ir.arminapp.musicplayer.db.DAO.LikedDAO
import ir.arminapp.musicplayer.db.DbHandler
import ir.arminapp.musicplayer.ext.MusicHelper
import ir.arminapp.musicplayer.recyclerView.model.MusicFile
import ir.arminapp.musicplayer.utils.MusicClickData


class MusicFragment : Fragment()  {
    private lateinit var binding: FragmentMusicFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMusicFragmentBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showMusic()
    }



    private fun showMusic() {

    }

}