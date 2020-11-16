package com.example.simplechat.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.simplechat.R
import com.example.simplechat.entity.ChatMessage

class ChatAdapter(activity: Activity, val resourceId:Int, data:List<ChatMessage>, name: String): ArrayAdapter<ChatMessage>(activity, resourceId, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        if(null == convertView){
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
        }else{
            view = convertView
        }
        val chatMsg : TextView = view.findViewById(R.id.chat_message)

        val chat = getItem(position)
        if(null != chat){
            chatMsg.text = chat.msg
        }
        return view
    }
}