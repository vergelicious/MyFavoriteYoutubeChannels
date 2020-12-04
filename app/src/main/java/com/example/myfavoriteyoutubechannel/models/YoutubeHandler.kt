package com.example.myfavoriteyoutubechannel.models

import com.example.myfavoriteyoutubechannel.models.Youtube
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class YoutubeHandler {
    var database: FirebaseDatabase
    var youtubeVideosReference: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        youtubeVideosReference = database.getReference("Youtube_Channels")
    }

    fun create(youtubeVideo: Youtube): Boolean{
        val id = youtubeVideosReference.push().key
        youtubeVideo.id = id

        youtubeVideosReference.child(id!!).setValue(youtubeVideo)


        return true
    }

    fun update(youtubeVideo: Youtube):Boolean {
        youtubeVideosReference.child(youtubeVideo.id!!).setValue(youtubeVideo)
        return true
    }

    fun delete(youtubeVideo: Youtube): Boolean {
        youtubeVideosReference.child(youtubeVideo.id!!).removeValue()
        return true
    }
}