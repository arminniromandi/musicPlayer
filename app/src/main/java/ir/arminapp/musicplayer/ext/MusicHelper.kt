package ir.arminapp.musicplayer.ext
import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import ir.arminapp.musicplayer.recyclerView.model.MusicFile


class MusicHelper(private val context: Context) {
    fun getAllMusic(): ArrayList<MusicFile> {
        val musicList = ArrayList<MusicFile>()
        val contentResolver: ContentResolver = context.contentResolver
        val uri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID,
            MediaStore.Audio.Media.SIZE
        )

        val cursor: Cursor? = contentResolver.query(uri, projection, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val titleColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val artistColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)
            val pathColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media.DATA)
            val albumIdColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)
            val fileSizeColumn: Int = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE)
            var id = 0
            do {
                id++
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                val path = cursor.getString(pathColumn)
                val albumId = cursor.getLong(albumIdColumn)
                val fileSize = cursor.getLong(fileSizeColumn)

                val albumArt = getAlbumArt(albumId)
                val musicFile = MusicFile(id,title, artist, path, albumArt, fileSize)
                musicList.add(musicFile)

            } while (cursor.moveToNext())

            cursor.close()
        }

        return musicList
    }

    private fun getAlbumArt(albumId: Long): String {
        val artUri: Uri = Uri.parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(artUri, albumId).toString()
    }
}


