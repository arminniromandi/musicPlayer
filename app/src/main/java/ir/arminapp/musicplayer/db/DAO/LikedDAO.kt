package ir.arminapp.musicplayer.db.DAO

import android.content.ContentValues
import android.database.Cursor
import android.util.Log
import ir.arminapp.musicplayer.recyclerView.model.MusicFile
import ir.arminapp.musicplayer.db.DbHandler

class LikedDAO(
    private val db : DbHandler
) {


    private val contentValues = ContentValues()
    private lateinit var cursor: Cursor


    fun save(like : MusicFile): Boolean {
        val database = db.writableDatabase
        setContentValues(like)
        val result = database.insert(DbHandler.LIKE_TABLE, null, contentValues)

        database.close()
        return result > 0
    }




    fun delete(id :Int):Boolean{
        val database = db.writableDatabase

        val result = database.delete(
            DbHandler.LIKE_TABLE,
            "${DbHandler.LIKE_ID} = ? ",
            arrayOf(id.toString())
        )
        database.close()

        return result > 0
    }


    fun getLikeById(id: Int): MusicFile {

        val dataBase = db.readableDatabase
        val query = "SELECT * FROM ${DbHandler.LIKE_TABLE} WHERE ${DbHandler.LIKE_ID} = ?"
        cursor = dataBase.rawQuery(query, arrayOf(id.toString()))
        val data = getAllData()
        cursor.close()
        dataBase.close()

        return data
    }

    private fun getAllData(): MusicFile {

        val data = MusicFile(0, "", "", "", "" , 0)

        try {

            if (cursor.moveToFirst()) {
                data.id = cursor.getInt(getIndex(DbHandler.LIKE_ID))
                data.title = cursor.getString(getIndex(DbHandler.LIKE_TITLE))
                data.artist = cursor.getString(getIndex(DbHandler.LIKE_ARTIST))
                data.path = cursor.getString(getIndex(DbHandler.LIKE_PATH))
                data.albumArt = cursor.getString(getIndex(DbHandler.LIKE_ALBUMART))
                data.fileSize = cursor.getLong(getIndex(DbHandler.LIKE_FILESIZE))

            }

        } catch (e: Exception) {
            Log.e("error", e.message.toString())
        }

        return data
    }


    //تمامی فیلد ها را برگرداند
    fun getAllLike(): ArrayList<MusicFile> {


        val dataBase = db.readableDatabase
        val query = "SELECT *" +
                "  FROM ${DbHandler.LIKE_TABLE}"

        cursor = dataBase.rawQuery(query, arrayOf())

        val data = getDataForRecycler()

        cursor.close()
        dataBase.close()


        return data


    }


    private fun getDataForRecycler(): ArrayList<MusicFile> {
        val data = ArrayList<MusicFile>()
        try {
            if (cursor.moveToFirst()) {
                do {
                    var id = cursor.getInt(getIndex(DbHandler.LIKE_ID))
                    var title = cursor.getString(getIndex(DbHandler.LIKE_TITLE))
                    var artist = cursor.getString(getIndex(DbHandler.LIKE_ARTIST))
                    var path = cursor.getString(getIndex(DbHandler.LIKE_PATH))
                    var albumArt = cursor.getString(getIndex(DbHandler.LIKE_ALBUMART))
                    var fileSize = cursor.getLong(getIndex(DbHandler.LIKE_FILESIZE))

                    data.add(MusicFile(id, title, artist, path, albumArt, fileSize))
                } while (cursor.moveToNext())


            }


        } catch (e: Exception) {
            Log.e("NOT_FOUND_INDEX", e.message.toString())
        }

        return data
    }

    private fun getIndex(name: String) = cursor.getColumnIndex(name)


    private fun setContentValues(like: MusicFile) {
        contentValues.clear()

        contentValues.put(DbHandler.LIKE_ID, like.id)
        contentValues.put(DbHandler.LIKE_TITLE,like.title )
        contentValues.put(DbHandler.LIKE_ARTIST, like.artist)
        contentValues.put(DbHandler.LIKE_PATH, like.path)
        contentValues.put(DbHandler.LIKE_ALBUMART, like.albumArt)
        contentValues.put(DbHandler.LIKE_FILESIZE, like.fileSize)
    }



}