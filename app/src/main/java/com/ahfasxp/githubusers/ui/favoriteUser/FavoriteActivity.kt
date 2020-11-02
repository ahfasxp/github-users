package com.ahfasxp.githubusers.ui.favoriteUser

import MappingHelper
import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahfasxp.githubusers.R
import com.ahfasxp.githubusers.data.Favorite
import com.ahfasxp.githubusers.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ahfasxp.githubusers.ui.detailUser.DetailUserActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter: FavoriteAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        supportActionBar?.title = "Favorite"

        rv_list_favorite.layoutManager = LinearLayoutManager(this)
        rv_list_favorite.setHasFixedSize(true)
        adapter = FavoriteAdapter(this)
        rv_list_favorite.adapter = adapter
        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Favorite) {
                showFavorite(data)
            }
        })

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }

        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            loadFavoritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                adapter.listFavorite = list
            }
        }
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val favorites = deferredFavorites.await()
            progressBar.visibility = View.INVISIBLE
            if (favorites.size > 0) {
                adapter.listFavorite = favorites
            } else {
                adapter.listFavorite = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listFavorite)
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_list_favorite, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showFavorite(favorite: Favorite) {
        Toast.makeText(this, "Kamu memilih ${favorite.username}", Toast.LENGTH_SHORT).show()
        val moveDetailUserIntent = Intent(this, DetailUserActivity::class.java)
        val selectedUser = favorite.username
        moveDetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, selectedUser)
        startActivity(moveDetailUserIntent)
    }

}