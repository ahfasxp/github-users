package com.ahfasxp.githubusers.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ahfasxp.githubusers.data.SearchUser
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
    val listUsers = MutableLiveData<ArrayList<SearchUser>>()

    fun setUser(username: String) {
        val listItems = ArrayList<SearchUser>()
        val url = "https://api.github.com/search/users?q=$username"
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
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val dataUser = list.getJSONObject(i)
                        val user = SearchUser()
                        user.name = dataUser.getString("login")
                        user.username = dataUser.getString("login")
                        user.avatar = dataUser.getString("avatar_url")
                        listItems.add(user)
                    }

                    //set data ke adapter
                    listUsers.postValue(listItems)
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

    fun getUser(): LiveData<ArrayList<SearchUser>> {
        return listUsers
    }
}