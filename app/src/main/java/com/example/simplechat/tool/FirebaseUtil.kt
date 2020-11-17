package com.example.simplechat.tool

import android.content.ContentValues.TAG
import android.util.Log
import com.example.simplechat.entity.Contact
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import java.sql.Timestamp


object FirebaseUtil {
    val tag : String = "Firebase"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    val KEY_NAME : String = "name"
    val KEY_NUMBER : String = "phoneNumber"
    val KEY_PWD : String = "password"
    val USER_1 : String = "user1"
    val USER_2 : String = "user2"

    //    private lateinit var refListener: ListenerRegistration
    val CONTACT_NAME: String = "Contact-Name"

    fun initFireBase(){
        Log.i(tag, "Firebase initializing......")
    }


    fun uploadContact(contact:Contact, name:String) : Boolean{
        Log.i(tag, "upload testing......")

        var isSuccess: Boolean = true
        val user: MutableMap<String, Any> = HashMap()
        user[KEY_NAME] = contact.name.toString()
        user[KEY_NUMBER] = contact.phoneNumber.toString()
        user[KEY_PWD] = contact.password.toString()


        if (null == contact.name) return false
        db.collection("Contact").document(contact.name)
            .set(contact)
            .addOnSuccessListener { documentReference ->
                Log.i(tag, "DocumentSnapshot added with ID: ")
                isSuccess = true
            }
            .addOnFailureListener { e ->
                Log.i(tag, "Error adding document", e)
                isSuccess = false
            }
        return isSuccess
    }


    fun loadContact(name: String) : Contact{
        Log.i(tag, "retrieve testing......")
        var contact: Contact = Contact()

        var docRef = db.collection("Contact").document(name)
        docRef.get().addOnSuccessListener {
            if(it.exists()){
                contact = Contact(it.getString(KEY_NAME),it.getString(KEY_NUMBER),it.getString(KEY_PWD) )
                Log.i(tag, "Name:" + it.getString(KEY_NAME))
                Log.i(tag, "Message:" + it.getString(KEY_NUMBER))
                Log.i(tag, "Password:" + it.getString(KEY_PWD))
            }else{
                Log.i(tag, "Not exist")
            }
        }.addOnFailureListener {
            Log.i(tag, "Error load document") }
        return contact
    }

    fun addListener(name: String){
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





}