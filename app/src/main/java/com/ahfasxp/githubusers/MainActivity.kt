package com.ahfasxp.githubusers

import android.content.Context
import android.content.Intent
import android.content.Intent.EXTRA_USER
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import android.app.SearchManager as SearchManager

class MainActivity : AppCompatActivity() {

    private var list = ArrayList<User>()
    private lateinit var adapter: SearchUserAdapter
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //recycleview list dari string.xml
        rv_list.setHasFixedSize(true)
        list.addAll(getListUsers())
        val listUserAdapter = UserAdapter(list)
        rv_list.layoutManager = LinearLayoutManager(this)
        rv_list.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                showUser(data)
            }
        })

        //recycleview dari api
        adapter = SearchUserAdapter()
        adapter.notifyDataSetChanged()

        //inisialisasi viewmodel tapi tetep tidak bisa menjaga data saat terjadi pergantian orientasi
        mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        mainViewModel.getUser().observe(this, Observer { searchUser ->
            if (searchUser != null) {
                adapter.setData(searchUser)
                showLoading(false)
            }
        })

    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }


    private fun getListUsers(): ArrayList<User> {
        val dataName = resources.getStringArray(R.array.name)
        val dataUsername = resources.getStringArray(R.array.username)
        val dataLocation = resources.getStringArray(R.array.location)
        val dataAvatar = resources.obtainTypedArray(R.array.avatar)
        val dataRepository = resources.getStringArray(R.array.repository)
        val dataCompany = resources.getStringArray(R.array.company)
        val dataFollowers = resources.getStringArray(R.array.followers)
        val dataFollowing = resources.getStringArray(R.array.following)

        val listUser = ArrayList<User>()
        for (position in dataName.indices) {
            var user = User(
                dataAvatar.getResourceId(position, -1),
                dataName[position],
                dataUsername[position],
                dataLocation[position],
                dataRepository[position],
                dataCompany[position],
                dataFollowers[position],
                dataFollowing[position]
            )
            listUser.add(user)
        }
        return listUser
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.insert_username)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            /*
            Gunakan method ini ketika search selesai atau OK
             */
            override fun onQueryTextSubmit(query: String): Boolean {
                //recycleview dari search api
                val username = query.toString()
                showLoading(true)
                mainViewModel.setUser(username)
                rv_list.layoutManager = LinearLayoutManager(this@MainActivity)
                rv_list.adapter = adapter

                adapter.setOnItemClickCallback(object : SearchUserAdapter.OnItemClickCallback{
                    override fun onItemClicked(data: SearchUser) {
                        showSearchUser(data)
                    }
                })

                return true
            }

            /*
            Gunakan method ini untuk merespon tiap perubahan huruf pada searchView
             */
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showSearchUser(user: SearchUser) {
        Toast.makeText(this, "Kamu memilih ${user.username}", Toast.LENGTH_SHORT).show()
        val moveDetailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        var selectedUser = user.username
        moveDetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, selectedUser)
        startActivity(moveDetailUserIntent)
    }

    private fun showUser(user: User) {
        Toast.makeText(this, "Kamu memilih ${user.name}", Toast.LENGTH_SHORT).show()
        val moveDetailUserIntent = Intent(this@MainActivity, DetailUserActivity::class.java)
        var selectedUser = user.username
        moveDetailUserIntent.putExtra(DetailUserActivity.EXTRA_USER, selectedUser)
        startActivity(moveDetailUserIntent)
    }

}