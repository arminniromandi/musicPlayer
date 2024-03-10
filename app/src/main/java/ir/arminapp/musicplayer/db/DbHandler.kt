package ir.arminapp.musicplayer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DbHandler(
    context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "liked.db"
        const val DB_VERSION = 1


        const val LIKE_TABLE = "LIKE_TABLE"
        const val LIKE_ID = "id"
        const val LIKE_TITLE = "title"
        const val LIKE_ARTIST = "artist"
        const val LIKE_PATH = "path"
        const val LIKE_ALBUMART = "albumArt"
        const val LIKE_FILESIZE = "fileSize"


    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(
            "CREATE TABLE IF NOT EXISTS $LIKE_TABLE (" +
                    "$LIKE_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$LIKE_TITLE VARCHAR(100)," +
                    "$LIKE_ARTIST VARCHAR(255)," +
                    "$LIKE_PATH TEXT," +
                    "$LIKE_ALBUMART VARCHAR(255)," +
                    "$LIKE_FILESIZE VARCHAR(50))"
        )
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }


}
