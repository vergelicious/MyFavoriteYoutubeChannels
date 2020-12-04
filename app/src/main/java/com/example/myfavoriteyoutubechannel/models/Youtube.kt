package com.example.myfavoriteyoutubechannel.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Youtube(var id: String? = "",var channel: String? = "", var link: String? = "", var rank: Int = 0, var reason: String? = "") {
    override fun toString(): String {
        return "($rank)\t $channel \nLink: $link\nReason: $reason"
    }


}