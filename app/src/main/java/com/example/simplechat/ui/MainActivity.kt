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
import com.example.simplechat.entity.Contact
import com.example.simplechat.tool.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class MainActivity : AppCompatActivity() {
    private lateinit var contactList : ListView
    private val contacts = ArrayList<Contact>()

    private lateinit var db: FirebaseFirestore
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

    override fun onStart() {
        super.onStart()
        initFireBase()
        loadDataTest()
    }

    /** Initialize main view**/
    fun initView() {
        val adapter = ContactAdapter(this,R.layout.contact_item, contacts)
        contactList = findViewById(R.id.list_main)
        contactList.adapter = adapter
        contactList.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, ChatActivity::class.java)
            startActivity(intent)
        }
    }

    fun test(){
        FirebaseUtil.initFireBase()
//        FirebaseUtil.uploadDataTest()
        FirebaseUtil.loadDataTest()
    }

    /** Request for permission and initialize data**/
    fun initData(){
        // Get Contact permission at runtime
        AndPermission.with(this)
            .runtime()
            //Permission.READ_CONTACTS, Permission.WRITE_CONTACTS, Permission.GET_ACCOUNTS
            .permission(Permission.Group.CONTACTS)
            .onGranted(Action { permissions: List<String?>? ->
                getContacts()
                Log.e("Main",contacts.toString())
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

    fun initFireBase(){
        Log.i(tag, "Main Firebase initializing......")

        db =  FirebaseFirestore.getInstance()
        var docRef = db.collection("Contact").document("contact")
        docRef.addSnapshotListener{snapshot, e ->
            if (e != null){
                Log.i(tag, "Update Error! ")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                Log.i(tag, "Message:" + snapshot.getString(KEY_NAME))
                Log.i(tag, "Message:" + snapshot.getString(KEY_NUMBER))
            } else {
                Log.d(tag, "Current data: null")
            }
        }
    }

    fun loadDataTest(){
        Log.i(tag, "retrieve testing......")

        var docRef = db.collection("Contact").document("contact")
        docRef.get().addOnSuccessListener {
            if(it.exists()){
                Log.i(tag, "Message:" + it.getString(KEY_NAME))
                Log.i(tag, "Message:" + it.getString(KEY_NUMBER))
            }else{
                Log.i(tag, "Null exist")
            }
        }.addOnFailureListener {
            Log.i(tag, "Error load document") }

    }


}