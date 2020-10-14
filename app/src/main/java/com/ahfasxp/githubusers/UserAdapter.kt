package com.ahfasxp.githubusers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

//import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var users = arrayListOf<User>()

    override fun getCount(): Int = users.size

    override fun getItem(position: Int): Any = users[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup?): View {
        var itemView = view

        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_user, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)
        val user = getItem(position) as User
        viewHolder.bind(user)
        return itemView
    }

    private inner class ViewHolder internal constructor(view: View) {
        private val txtName: TextView = view.findViewById(R.id.txt_name)
        private val txtUsername: TextView = view.findViewById(R.id.txt_username)
        private val txtLocation: TextView = view.findViewById(R.id.txt_location)
        private val imgAvatar: ImageView = view.findViewById(R.id.img_avatar)

        internal fun bind(user: User) {
            txtName.text = user.name
            txtUsername.text = user.username
            txtLocation.text = user.location
            imgAvatar.setImageResource(user.avatar)
//            Tidak bisa menggunkan kotlin extension
//            txt_name.text = user.name
//            txt_username.text = user.username
//            txt_location.text = user.location
//            img_avatar.setImageResource(user.avatar)
        }
    }
}