package ir.arminapp.musicplayer.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.arminapp.musicplayer.databinding.FragmentPlayingBinding
import ir.arminapp.musicplayer.db.DAO.LikedDAO
import ir.arminapp.musicplayer.db.DbHandler
import ir.arminapp.musicplayer.recyclerView.adapter.AdapterLike
import ir.arminapp.musicplayer.recyclerView.adapter.AdapterMusic


class FavoriteFragment : Fragment() {
    private lateinit var binding: FragmentPlayingBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayingBinding.inflate(layoutInflater)

        val db = LikedDAO(DbHandler(requireContext()))
        val likes = db.getAllLike()

        binding.recyclerView.layoutManager = LinearLayoutManager(
            requireContext(),
            RecyclerView.VERTICAL,
            false
        )

        val adapter = AdapterLike(requireActivity(), likes , db)
        binding.recyclerView.adapter = adapter


        return binding.root
    }


}