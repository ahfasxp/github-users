package com.ahfasxp.githubusers.db


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.ahfasxp.githubusers.db.DatabaseContract.FavoriteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABSE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbfavorite"
        private const val DATABSE_VERSION = 1

        private val SQL_CREATE_TABLE_FAVORIT = "CREATE TABLE $TABLE_NAME" +
                "(${DatabaseContract.FavoriteColumns.ID} INTEGER PRIMARY KEY," +
                "${DatabaseContract.FavoriteColumns.USERNAME} TEXT NOT NULL," +
                "${DatabaseContract.FavoriteColumns.AVATAR} TEXT NOT NULL," +
                "${DatabaseContract.FavoriteColumns.DATE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_FAVORIT)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}