package com.ahfasxp.githubusers

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_user.*
import kotlinx.android.synthetic.main.activity_detail_user.progressBar
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

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
                    Glide.with(this@DetailUserActivity)
                        .load(responseObject.getString("avatar_url"))
                        .into(img_profil)
                    tv_name.text = responseObject.getString("name")
                    tv_username.text = responseObject.getString("login")
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

}