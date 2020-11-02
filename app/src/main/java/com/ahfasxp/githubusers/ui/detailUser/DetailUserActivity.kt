package com.ahfasxp.githubusers.ui.detailUser

import android.content.ContentValues
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ahfasxp.githubusers.R
import com.ahfasxp.githubusers.data.Favorite
import com.ahfasxp.githubusers.data.SearchUser
import com.ahfasxp.githubusers.db.DatabaseContract
import com.ahfasxp.githubusers.db.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ahfasxp.githubusers.db.DatabaseContract.FavoriteColumns.Companion.DATE
import com.ahfasxp.githubusers.db.FavoriteHelper
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_user.*
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class DetailUserActivity : AppCompatActivity(), View.OnClickListener {
    private var favorite: Favorite? = null
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var uriWithId: Uri
    private var position: Int = 0
    private var isEdit = false
    var id: Int? = null
    var avatar: String? = null
    var uname: String? = null

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val username = intent.getStringExtra(EXTRA_USER).toString()
        showLoading(true)
        setUser(username)

        //kirim data username ke fragment follower dan following
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        //menympan data userfavorite
        btFavorite.visibility = View.VISIBLE
        val favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        if (favoriteHelper.check(username)){
            btFavorite.visibility = View.GONE
            btUnFavorite.visibility = View.VISIBLE
        }

        //button favorite
        btFavorite.setOnClickListener {
            val username = uname
            val avatar = avatar
            val id = id

            favorite?.id = id
            favorite?.username = username
            favorite?.avatar = avatar
            favorite?.date = getCurrentDate()

            val values = ContentValues()
            values.put(DatabaseContract.FavoriteColumns.ID, id)
            values.put(DatabaseContract.FavoriteColumns.USERNAME, username)
            values.put(DatabaseContract.FavoriteColumns.AVATAR, avatar)
            values.put(DATE, getCurrentDate())
            contentResolver.insert(CONTENT_URI, values)
            btFavorite.visibility = View.INVISIBLE
            btUnFavorite.visibility = View.VISIBLE
            Toast.makeText(this, "User Favorite", Toast.LENGTH_SHORT).show()
        }

        //button unfavorite
        btUnFavorite.setOnClickListener {
            contentResolver.delete(Uri.parse(CONTENT_URI.toString() + "/" + favorite?.id),null,null)
            btFavorite.visibility = View.VISIBLE
            btUnFavorite.visibility = View.INVISIBLE
            Toast.makeText(this, "User Unfavorite", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun setUser(username: String) {
        val listItems = ArrayList<SearchUser>()
        val url = "https://api.github.com/users/$username"
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token ad81bb7c78a5023aa15e58403d1cb5b8ac813d32")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    //parsing json
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    //mengisi var id, username, avatar dengan api
                    id = responseObject.getInt("id")
                    avatar = responseObject.getString("avatar_url")
                    uname = responseObject.getString("login")
                    Glide.with(this@DetailUserActivity)
                        .load(avatar)
                        .into(img_profil)
                    tv_name.text = responseObject.getString("name")
                    tv_username.text = uname
                    tv_location.text = responseObject.getString("location")
                    tv_repository.text = responseObject.getInt("public_repos").toString()
                    tv_company.text = responseObject.getString("company")
                    val follower = responseObject.getInt("followers").toString()
                    val following = responseObject.getInt("following").toString()
                    tv_followers.text = getString(R.string.user_followers, follower, following)
                    showLoading(false)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}