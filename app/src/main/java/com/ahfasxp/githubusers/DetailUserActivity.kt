package com.ahfasxp.githubusers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailUserActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)


    }
}