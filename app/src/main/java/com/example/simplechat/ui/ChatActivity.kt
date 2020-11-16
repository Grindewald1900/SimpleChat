package com.example.simplechat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import com.example.simplechat.R
import com.example.simplechat.data.ChatAdapter
import com.example.simplechat.data.ContactAdapter
import com.example.simplechat.entity.ChatMessage
import com.example.simplechat.entity.Contact

class ChatActivity : AppCompatActivity() {
    private lateinit var chatList : ListView
    private val chats = ArrayList<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initView()
    }

    /** Initialize main view**/
    fun initView() {
        chats.add(ChatMessage("aaaaa", "2020-01-01"))
        val adapter = ChatAdapter(this,R.layout.chat_item, chats, "Yee")
        chatList = findViewById(R.id.list_chat)
        chatList.adapter = adapter
        chatList.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}