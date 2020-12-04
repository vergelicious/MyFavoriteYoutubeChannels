package com.example.myfavoriteyoutubechannel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.myfavoriteyoutubechannel.models.Youtube
import com.example.myfavoriteyoutubechannel.models.YoutubeHandler
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

lateinit var youtubeHandler: YoutubeHandler

class MainActivity : AppCompatActivity() {
    lateinit var channelEditText: EditText
    lateinit var linkEditText: EditText
    lateinit var rankEditText: EditText
    lateinit var reasonEditText: EditText
    lateinit var button: Button
    lateinit var youtubeVideos: ArrayList<Youtube>
    lateinit var youtubeVideosArrayAdapter: ArrayAdapter<Youtube>
    lateinit var youtubeVideosListView: ListView
    lateinit var youtubeVideoGettingEdited: Youtube



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        channelEditText = findViewById(R.id.videoChannelEditText)
        linkEditText = findViewById(R.id.videoLinkEditText)
        rankEditText = findViewById(R.id.videoRankEditText)
        reasonEditText = findViewById(R.id.reasonEditText)
        button = findViewById(R.id.button)
        youtubeVideosListView = findViewById(R.id.listView)

        youtubeHandler = YoutubeHandler()
        youtubeVideos = ArrayList()


        button.setOnClickListener{
            val channel = channelEditText.text.toString()
            val link = linkEditText.text.toString()
            var rank: Int
            if (rankEditText.text.toString() == "" ) {
                rank = 0
            } else {
                rank = rankEditText.text.toString().toInt()
            }


            val reason = reasonEditText.text.toString()


            if(button.text.toString() == "Add"){
                val restaurant = Youtube(channel = channel , link = link ,rank =  rank, reason = reason  )
                if(youtubeHandler.create(restaurant)){
                    Toast.makeText(this, "Youtube video added.", Toast.LENGTH_SHORT).show()
                }
                clear()
            } else if(button.text.toString() == "Update") {
                val restaurant = Youtube( id = youtubeVideoGettingEdited.id, channel = channel , link = link ,rank =  rank, reason = reason )
                if(youtubeHandler.update(restaurant)){
                    Toast.makeText(this, "Youtube video updated.", Toast.LENGTH_SHORT).show()
                }
                clear()
            }
        }
        registerForContextMenu(youtubeVideosListView)
    }
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId){
            R.id.editYoutube -> {
                youtubeVideoGettingEdited = youtubeVideos[info.position]
                channelEditText.setText(youtubeVideoGettingEdited.channel)
                linkEditText.setText(youtubeVideoGettingEdited.link)
                rankEditText.setText(youtubeVideoGettingEdited.rank.toString())
                reasonEditText.setText(youtubeVideoGettingEdited.reason)
                button.setText("Update")
                return true
            }
            R.id.deleteYoutube -> {
                if (youtubeHandler.delete(youtubeVideos[info.position])){
                    Toast.makeText(this, "Youtube video updated", Toast.LENGTH_SHORT).show()
                }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    override fun onStart() {
        super.onStart()
        youtubeHandler.youtubeVideosReference.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                youtubeVideos.clear()
                p0.children.forEach {
                        it -> val video = it.getValue(Youtube::class.java)
                    youtubeVideos.add(video!!)
                    youtubeVideos.sortWith(object: Comparator<Youtube>{
                        override fun compare(o1: Youtube, o2: Youtube): Int = when {
                            o1.rank > o2.rank -> 1
                            o1.rank == o2.rank -> 0
                            else -> -1
                        }
                    })

                }
                youtubeVideosArrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, youtubeVideos)
                youtubeVideosListView.adapter = youtubeVideosArrayAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
    fun clear(){
        channelEditText.text.clear()
        linkEditText.text.clear()
        rankEditText.text.clear()
        reasonEditText.text.clear()
        button.setText("Add")
    }
}