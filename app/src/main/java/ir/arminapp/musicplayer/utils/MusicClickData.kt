package ir.arminapp.musicplayer.utils

import ir.arminapp.musicplayer.recyclerView.model.MusicFile

interface MusicClickData {
    fun playMusic(musicFile: MusicFile){}
}