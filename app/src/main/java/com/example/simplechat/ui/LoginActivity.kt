package com.example.simplechat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.simplechat.R
import com.example.simplechat.data.SharedPreferenceUtil
import com.example.simplechat.entity.Contact
import com.example.simplechat.tool.FirebaseUtil
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    /** Initialize main view**/
    fun initView() {
        bt_login_sign.setOnClickListener {
            et_login_name.text.ifEmpty {
                Toast.makeText(this, resources.getString(R.string.empty_name), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            et_login_pwd.text.ifEmpty {
                Toast.makeText(this, resources.getString(R.string.empty_pwd), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            loadContact(et_login_name.text.toString())

        }
    }

    fun loadContact(name: String) {
        var docRef = db.collection("Contact").document(name)
        docRef.get().addOnSuccessListener {
            if(it.exists()){
                if(it.getString(FirebaseUtil.KEY_PWD) == et_login_pwd.text.toString()){
                    SharedPreferenceUtil.setSharedPreference(this, SharedPreferenceUtil.USER_NAME, et_login_name.text.toString())
                    var intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(SharedPreferenceUtil.USER_NAME, et_login_name.text.toString())
                    startActivity(intent)
                }else{
                    Toast.makeText(this, resources.getString(R.string.wrong_pwd), Toast.LENGTH_LONG).show()
                }
                Log.i(FirebaseUtil.tag, "Name:" + it.getString(FirebaseUtil.KEY_NAME))
                Log.i(FirebaseUtil.tag, "Message:" + it.getString(FirebaseUtil.KEY_NUMBER))
                Log.i(FirebaseUtil.tag, "Password:" + it.getString(FirebaseUtil.KEY_PWD))
            }else{
                Log.i(FirebaseUtil.tag, "Not exist")
            }
        }.addOnFailureListener {
            Log.i(FirebaseUtil.tag, "Error load document") }
    }
}