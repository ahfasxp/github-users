import android.database.Cursor
import com.ahfasxp.favoriteapp.DatabaseContract
import com.ahfasxp.favoriteapp.Favorite

object MappingHelper {

    fun mapCursorToArrayList(favoritesCursor: Cursor?): ArrayList<Favorite> {
        val favoritesList = ArrayList<Favorite>()
        favoritesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DATE))
                favoritesList.add(Favorite(id, username, avatar, date))
            }
        }
        return favoritesList
    }

    fun mapCursorToObject(favoritesCursor: Cursor?): Favorite {
        var favorite = Favorite()
        favoritesCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.AVATAR))
            val date = getString(getColumnIndexOrThrow(DatabaseContract.FavoriteColumns.DATE))
            favorite = Favorite(id, username, avatar, date)
        }
        return favorite
    }
}