package com.example.simplechat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.example.simplechat.R
import com.example.simplechat.data.ChatAdapter
import com.example.simplechat.data.SharedPreferenceUtil
import com.example.simplechat.entity.Chat
import com.example.simplechat.entity.ChatMessage
import com.example.simplechat.entity.Contact
import com.example.simplechat.tool.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_chat.*
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat

class ChatActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private lateinit var chatList : ListView
    private val chats = ArrayList<ChatMessage>()
    private lateinit var contactName : String
    private lateinit var userName : String
    private var chatId :String = ""
    private var isOpen : Boolean = false
    private var startTime : Long = 0
    private var endTime : Long = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        initData()
    }

    /** Initialize main view**/
    fun initView() {
        Log.i(FirebaseUtil.tag, "initView......")

        chatList = findViewById(R.id.list_chat)
        refreshList()
        chatList.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
        ib_chat_enter.setOnClickListener {
            var msg = et_chat_input.text.toString()
            et_chat_input.text.clear()
            uploadChat(userName, contactName, msg)
            startTime = System.currentTimeMillis()
        }
    }

    fun refreshList(){
        Log.i(FirebaseUtil.tag, "refreshList......")
        val adapter = ChatAdapter(this,R.layout.chat_item, chats, contactName)
        chatList.adapter = adapter
        chatList.smoothScrollToPositionFromTop(chats.size - 1, chatList.height - 100, 100)
        chatList.setSelection(chats.size - 1)
    }

    fun initData(){
        Log.i(FirebaseUtil.tag, "initData......")

        userName = SharedPreferenceUtil.getSharedPreference(this, SharedPreferenceUtil.USER_NAME, "")
        contactName = intent.getStringExtra(FirebaseUtil.CONTACT_NAME).toString()
        findChats()
    }

    fun findChats() {
        Log.i(FirebaseUtil.tag, "findChats......")

        var docRef = db.collection("Chat")
        docRef.get().addOnSuccessListener {
            for(item in it){
                Log.i(FirebaseUtil.tag, "Chats:" + item.id)
                if (item.id.contains(userName) && item.id.contains(contactName)){
                    chatId = item.id
                }
            }
            openChat()
        }.addOnFailureListener {
            Log.i(FirebaseUtil.tag, "Error load document") }
    }

    fun openChat(){
        Log.i(FirebaseUtil.tag, "openChat......")

//        chats.clear()
        if("" == chatId){
            chatId = "chat@$userName@$contactName"
        }
        var docRef = db.collection("Chat").document(chatId)
        if(!isOpen){
            addChatListener(chatId)
            isOpen = true
        }
        docRef.get().addOnSuccessListener {
            if(it.exists()){
                var str = it.data.toString().substring(1, it.data.toString().length - 1)
                var list:List<String> = str.split(",")
                for (item in list){
                    var info = item.split("=")
                    var timeInfo = info[0].split("@")[2]
                    var message = ChatMessage(timeInfo, info[1], userName)
                    if(!chats.contains(message)){
                        endTime = System.currentTimeMillis()
                        if(startTime.toInt() != 0){
                            var showTxt : String
                            if(message.msg.length > 5){
                                showTxt = message.msg.substring(0,5)
                            }else{
                                showTxt = message.msg
                            }
                            Toast.makeText(this, showTxt + "...\n" + "time to upload:" + (endTime-startTime).toInt() + " ms",Toast.LENGTH_LONG).show()
                        }
                        chats.add(message)
                    }
                }
                initView()
            }else{
                setChat(userName, contactName)
            }
        }.addOnFailureListener {
            Log.i(FirebaseUtil.tag, "Error load document") }
    }

    fun setChat(nameUser: String, nameContact:String){
        Log.i(FirebaseUtil.tag, "setChat......")

        Log.i(FirebaseUtil.tag, "Chat start......$nameUser @ $nameContact")
        var time = Timestamp(System.currentTimeMillis())
        var docRef = db.collection("Chat").document("chat@$nameUser@$nameContact")
        val chat: MutableMap<String, Any> = HashMap()
        chat["message@System@$time"] = "Welcome!"

        docRef.set(chat)
            .addOnSuccessListener { documentReference ->
                Log.i(FirebaseUtil.tag, "DocumentSnapshot added with ID: ")
            }
            .addOnFailureListener { e ->
                Log.i(FirebaseUtil.tag, "Error adding document", e)
            }
    }

    fun uploadChat(nameUser: String, nameContact:String, msg:String){
        Log.i(FirebaseUtil.tag, "uploadChat......")

        var time = Timestamp(System.currentTimeMillis())
        val chat: MutableMap<String, Any> = HashMap()
        chat["message@$nameUser@$time"] = msg

        db.collection("Chat").document(chatId).set(chat, SetOptions.merge())
            .addOnSuccessListener { documentReference ->
                Log.i(FirebaseUtil.tag, "DocumentSnapshot added with ID: ")

                openChat()
            }
            .addOnFailureListener { e ->
                Log.i(FirebaseUtil.tag, "Error adding document", e)
            }
    }

    fun addChatListener(name: String) {
        Log.i(FirebaseUtil.tag, "addChatListener......")

        var docRef = db.collection("Chat").document(name)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.i(FirebaseUtil.tag, "Update Error! ")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                openChat()
                Log.d(FirebaseUtil.tag, "Updated")
            } else {
                Log.d(FirebaseUtil.tag, "Current data: null")
            }
        }
    }
}