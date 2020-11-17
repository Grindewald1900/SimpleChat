package com.example.simplechat.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
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
        val photo1 : com.kproduce.roundcorners.CircleImageView = view.findViewById(R.id.chat_img)
        val photo2 : com.kproduce.roundcorners.CircleImageView = view.findViewById(R.id.chat_img2)
        val chatMsg : TextView = view.findViewById(R.id.chat_message)
        val chatTime : TextView = view.findViewById(R.id.chat_time)
        val chat = getItem(position)
        if(null != chat){
            if(chat.msg.contains(chat.userName)){
                photo2.visibility = View.INVISIBLE
            }else{
                photo1.visibility = View.INVISIBLE
            }
            chatMsg.text = chat.msg
            chatTime.text = chat.name
        }
        return view
    }
}