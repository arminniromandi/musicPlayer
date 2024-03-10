package ir.arminapp.musicplayer.recyclerView.model

data class MusicFile(
    var id: Int,
    var title: String,
    var artist: String,
    var path: String,
    var albumArt: String,
    var fileSize: Long
)

