package com.example.simplechat.data

import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Settings.Global.getString
import android.util.Log
import com.example.simplechat.R
import com.example.simplechat.tool.FirebaseUtil
import com.example.simplechat.tool.MainApplication
import com.example.simplechat.ui.MainActivity

object SharedPreferenceUtil {
    val USER_NAME = "USER_NAME"
    val USER_PHONE = "USER_PHONE"
    val SHARE_NAME = "SHARE"

    fun setSharedPreference(activity: Activity, key: String, value: String){
        val sharedPref = activity.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE)
        Log.i(FirebaseUtil.tag,"set share: " + value)
        sharedPref.edit().putString(key, value).apply()
        val a = 0
    }

    fun getSharedPreference(activity: Activity, key: String, default: String) :String{
        val sharedPref = activity.getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE)
        var str = sharedPref.getString(key, default) ?: ""
        return str
    }
}