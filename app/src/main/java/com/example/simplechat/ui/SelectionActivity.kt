package com.example.simplechat.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.simplechat.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.IdpResponse
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_selection.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.indeterminateProgressDialog

class SelectionActivity : AppCompatActivity() {
    private val RC_SIGN_IN = 1
    private val RC_LOGIN_IN = 2
    private val signInProviders = listOf(AuthUI.IdpConfig.EmailBuilder().setAllowNewAccounts(true).setRequireName(true).build())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)
        initView()
    }

    private fun initView(){
        account_sign_in.setOnClickListener {
//            val intent = AuthUI.getInstance().createSignInIntentBuilder()
//                .setAvailableProviders(signInProviders)
//                .setLogo(R.drawable.pic_chat)
//                .build()
            val intent = Intent(this, RegisterActivity::class.java )
            startActivity(intent)
        }
        account_login_in.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java )
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val progressDialog = indeterminateProgressDialog("Setting up your account")
//                startActivity(intentFor<MainActivity>().newTask().clearTask())
//                progressDialog.dismiss()

//                FirestoreTools.initCurrentUserIfFirstTime {
//                    startActivity(intentFor<MainActivity>().newTask().clearTask())
//
//                    val registrationToken = FirebaseInstanceId.getInstance().token
//                    MyFirebaseInstanceIDService.addTokenToFirestore(registrationToken)
//
//                    progressDialog.dismiss()
//                }
            }
            else if (resultCode == Activity.RESULT_CANCELED) {
                if (response == null) return

                when (response.error?.errorCode) {
                    ErrorCodes.NO_NETWORK ->
                        longSnackbar(constraint_layout, "No network")
                    ErrorCodes.UNKNOWN_ERROR ->
                        longSnackbar(constraint_layout, "Unknown error")
                }
            }
        }
    }
}