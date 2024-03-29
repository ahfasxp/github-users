package com.ahfasxp.githubusers.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Favorite(
    var id: Int? = null,
    var username: String? = null,
    var avatar: String? = null,
    var date: String? = null
) : Parcelable