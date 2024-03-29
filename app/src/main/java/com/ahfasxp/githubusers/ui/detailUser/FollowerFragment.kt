package com.ahfasxp.githubusers.ui.detailUser

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ahfasxp.githubusers.R
import com.ahfasxp.githubusers.data.SearchUser
import com.ahfasxp.githubusers.ui.searchUser.SearchUserAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_follower.*
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 * Use the [FollowerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FollowerFragment : Fragment() {
    private lateinit var adapter: SearchUserAdapter

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)
        Log.d("cekusername", username.toString())
        if (username != null) {
            setUser(username)
        }
        //recycleview dari api
        adapter = SearchUserAdapter()
        adapter.notifyDataSetChanged()
        rv_list_follower.layoutManager = LinearLayoutManager(activity)
        rv_list_follower.adapter = adapter
    }

    private fun setUser(username: String) {
        val listItems = ArrayList<SearchUser>()
        val url = "https://api.github.com/users/$username/followers"
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
                    val responsArray = JSONArray(result)
                    val list = responsArray

                    for (i in 0 until list.length()) {
                        val dataUser = list.getJSONObject(i)
                        val user = SearchUser()
                        user.name = dataUser.getString("login")
                        user.username = dataUser.getString("login")
                        user.avatar = dataUser.getString("avatar_url")
                        listItems.add(user)
                    }

                    //set data ke adapter
                    adapter.setData(listItems)
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