package com.example.simplechat.tool

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase


object FirebaseUtil {
    private lateinit var db: FirebaseFirestore
    private val tag : String = "Firebase"
    private val KEY_NAME : String = "name"
    private val KEY_NUMBER : String = "phoneNumber"
//    private lateinit var refListener: ListenerRegistration

    fun initFireBase(){
        Log.i(tag, "Firebase initializing......")

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

    fun uploadDataTest(){
        Log.i(tag, "upload testing......")

        val user: MutableMap<String, Any> = HashMap()
        user[KEY_NAME] = "Yee"
        user[KEY_NUMBER] = "91919191"

        db.collection("Contact").document("contact2")
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.i(tag, "DocumentSnapshot added with ID: ") }
            .addOnFailureListener { e ->
                Log.i(tag, "Error adding document", e) }
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