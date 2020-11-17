package com.example.simplechat.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simplechat.R
import com.example.simplechat.data.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_register.*

class AccountActivity : AppCompatActivity() {
    private lateinit var name:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        name = intent.getStringExtra(SharedPreferenceUtil.USER_NAME) ?: "null"
        bt_acc_name.text = name
        bt_acc_quit.setOnClickListener {
            SharedPreferenceUtil.setSharedPreference(this, SharedPreferenceUtil.USER_NAME,"")
            SharedPreferenceUtil.setSharedPreference(this, SharedPreferenceUtil.USER_PHONE, "")
            var intent = Intent(this, SelectionActivity::class.java)
            startActivity(intent)
        }
    }
}