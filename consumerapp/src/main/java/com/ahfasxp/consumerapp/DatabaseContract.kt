package com.ahfasxp.consumerapp

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    //kode content provider
    const val AUTHORITY = "com.ahfasxp.githubusers"
    const val SCHEME = "content"

    class FavoriteColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "favorite"
            const val ID = "_id"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val DATE = "date"

            // untuk membuat URI content://com.dicoding.picodiploma.mynotesapp/note
            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}