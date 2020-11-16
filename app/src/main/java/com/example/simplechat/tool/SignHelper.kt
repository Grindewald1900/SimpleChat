package com.example.simplechat.tool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplechat.ui.LoginActivity
import com.example.simplechat.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class SignHelper : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (FirebaseAuth.getInstance().currentUser == null)
            startActivity<MainActivity>()
//            startActivity<LoginActivity>()
        else
            startActivity<MainActivity>()
        finish()
    }
}