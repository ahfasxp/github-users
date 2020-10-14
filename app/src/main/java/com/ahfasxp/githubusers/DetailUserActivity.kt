package com.ahfasxp.githubusers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_user.*

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        var dataUser = intent.getParcelableExtra<User>(EXTRA_USER) as User
        supportActionBar?.title = dataUser.name
        img_avatar.setImageResource(dataUser.avatar)
        tv_name.text = dataUser.name
        tv_username.text = dataUser.username
        tv_location.text = dataUser.location
        tv_repository.text = dataUser.repository
        tv_company.text = dataUser.company
        tv_followers.text = dataUser.followers
        tv_following.text = dataUser.following
    }
}