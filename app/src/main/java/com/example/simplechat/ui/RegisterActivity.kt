package com.example.simplechat.ui

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.simplechat.R
import com.example.simplechat.data.SharedPreferenceUtil
import com.example.simplechat.entity.Contact
import com.example.simplechat.tool.FirebaseUtil
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var name:String
    private lateinit var pwd:String
    private lateinit var phoneNumber:String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initView()
    }

    fun initView(){
        bt_reg_sign.setOnClickListener {
            et_reg_name.text.ifEmpty {
                Toast.makeText(this, resources.getString(R.string.empty_name), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            et_reg_phone.text.ifEmpty {
                Toast.makeText(this, resources.getString(R.string.empty_phone), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (et_reg_pwd.text.toString() != et_reg_pwd2.text.toString()) {
                Toast.makeText(this, resources.getString(R.string.check_pwd), Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            var isSuccss = FirebaseUtil.uploadContact(Contact(et_reg_name.text.toString(), et_reg_phone.text.toString(), et_reg_pwd.text.toString()), et_reg_name.text.toString())
            Log.i(FirebaseUtil.tag, "Is success: " + isSuccss)
            if (isSuccss){
                SharedPreferenceUtil.setSharedPreference(this, SharedPreferenceUtil.USER_NAME, et_reg_name.text.toString())
                SharedPreferenceUtil.setSharedPreference(this, SharedPreferenceUtil.USER_PHONE, et_reg_phone.text.toString())
                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra(SharedPreferenceUtil.USER_NAME, et_reg_name.text.toString())
                startActivity(intent)
            }else{
                Toast.makeText(this, resources.getString(R.string.network_err), Toast.LENGTH_LONG).show()
            }
        }
    }
}