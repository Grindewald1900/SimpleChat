package com.example.simplechat.tool

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.simplechat.data.SharedPreferenceUtil
import com.example.simplechat.ui.MainActivity
import com.example.simplechat.ui.SelectionActivity
import org.jetbrains.anko.startActivity

class SignHelper : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.i(FirebaseUtil.tag, "User name: " + SharedPreferenceUtil.getSharedPreference(this, SharedPreferenceUtil.USER_NAME, ""))
        if (!SharedPreferenceUtil.getSharedPreference(this, SharedPreferenceUtil.USER_NAME, "").equals(""))
            startActivity<MainActivity>()
//            startActivity<LoginActivity>()
        else
            startActivity<SelectionActivity>()
        finish()
    }
}