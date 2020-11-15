package com.example.simplechat.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.simplechat.R
import com.example.simplechat.entity.Contact

class ContactAdapter(activity: Activity, val resourceId:Int, data:List<Contact>): ArrayAdapter<Contact>(activity, resourceId, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view : View
        if(null == convertView){
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
        }else{
            view = convertView
        }
        var contactImg : ImageView = view.findViewById(R.id.contact_img)
        val contactMsg : TextView = view.findViewById(R.id.contact_message)
        val contactName: TextView = view.findViewById(R.id.contact_name)

        val contact = getItem(position)
        if(null != contact){
            contactName.text = contact.name
        }
        return view
    }
}