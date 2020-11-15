package com.example.simplechat.ui

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import com.example.simplechat.R
import com.example.simplechat.data.ContactAdapter
import com.example.simplechat.entity.Contact
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

class MainActivity : AppCompatActivity() {
    private lateinit var listContact : ListView
    private val contacts = ArrayList<Contact>()

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
    }

    /** Initialize main view**/
    fun initView() {
        val adapter = ContactAdapter(this,R.layout.contact_item, contacts)
        listContact = findViewById(R.id.main_list)
        listContact.adapter = adapter
    }

    /** Request for permission and initialize data**/
    fun initData(){
        var builder = StringBuilder()
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


}