package com.example.simplechat.ui

import android.content.ContentResolver
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ListView
import com.example.simplechat.R
import com.example.simplechat.data.ContactAdapter
import com.example.simplechat.data.SharedPreferenceUtil
import com.example.simplechat.entity.Contact
import com.example.simplechat.tool.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var contactList : ListView
    private val contacts = ArrayList<Contact>()
    private lateinit var userName:String

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val tag : String = "Firebase"
    private val KEY_NAME : String = "name"
    private val KEY_NUMBER : String = "phoneNumber"

    companion object{
        private const val CONTACT_ID_INDEX: Int = 0
        // The column index for the CONTACT_KEY column
        private const val CONTACT_KEY_INDEX: Int = 1

        // Defines a variable for the search string
        private val searchString: String = ""
        // Defines the array to hold values that replace the ?
        private val selectionArgs = arrayOf<String>(searchString)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
//        test()
    }

    /** Initialize main view**/
    fun initView() {
        val adapter = ContactAdapter(this,R.layout.contact_item, contacts)
        userName= SharedPreferenceUtil.getSharedPreference(this, SharedPreferenceUtil.USER_NAME, "")
        if(userName == "") {
            userName = intent.getStringExtra(SharedPreferenceUtil.USER_NAME) ?: "null"
        }
        main_user_name.text = userName
        loadChats(userName)

        contactList = findViewById(R.id.list_main)
        contactList.adapter = adapter
        contactList.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(FirebaseUtil.CONTACT_NAME, contacts.get(position).name.toString())
            startActivity(intent)
        }
        main_header.setOnClickListener {
            var intent = Intent(this, AccountActivity::class.java)
            intent.putExtra(SharedPreferenceUtil.USER_NAME, userName)
            startActivity(intent)
        }
    }

    fun test(){
//        FirebaseUtil.initFireBase()
//        FirebaseUtil.uploadDataTest()
//        FirebaseUtil.loadDataTest()
    }

    /** Request for permission and initialize data**/
    fun initData(){
        FirebaseUtil.initFireBase()
        // Get Contact permission at runtime
        AndPermission.with(this)
            .runtime()
            //Permission.READ_CONTACTS, Permission.WRITE_CONTACTS, Permission.GET_ACCOUNTS
            .permission(Permission.Group.CONTACTS)
            .onGranted(Action { permissions: List<String?>? ->
                getContacts()
                initView()
            })
            .onDenied(Action { permissions: List<String?>? -> })
            .start()
    }



    /** Get contacts with content provider**/
    private fun getContacts() {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (null == cursor){
            return
        }
        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phoneNumber = (cursor.getString(
                    cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                if (phoneNumber > 0) {
                    val cursorPhone = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)
                        ?: continue

                    if(cursorPhone.count > 0) {
                        while (cursorPhone.moveToNext()) {
                            val phoneNumValue = cursorPhone.getString(
                                cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            contacts.add(Contact(name, phoneNumValue))
//                            FirebaseUtil.uploadContact(Contact(name, phoneNumValue), name)
                            Log.e("Name ===>",phoneNumValue);
                        }
                    }
                    cursorPhone.close()
                }
            }
        } else {
        }
        cursor.close()
        return
    }

    fun loadChats(uName: String) {
        Log.i(FirebaseUtil.tag, "Loading......")
        var chatList: ArrayList<String> = ArrayList()

        var docRef = db.collection("Chat")
        docRef.get().addOnSuccessListener {
            for(item in it){
                Log.i(FirebaseUtil.tag, "Chats:" + item.id)
                if (item.id.contains(userName)){
                    chatList.add(item.id)
                    addChatListener(item.id)
                }
            }

        }.addOnFailureListener {
            Log.i(FirebaseUtil.tag, "Error load document") }
    }

    fun addChatListener(name: String){
        var docRef = db.collection("Chat").document(name)
        docRef.addSnapshotListener{snapshot, e ->
            if (e != null){
                Log.i(FirebaseUtil.tag, "Update Error! ")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.d(FirebaseUtil.tag, "Updated")
            } else {
                Log.d(FirebaseUtil.tag, "Current data: null")
            }
        }
    }



}