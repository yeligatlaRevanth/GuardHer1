package com.example.guardher

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MyAdapter (var mCtx: Context, var resources: Int, var items: List<UserBook> ) :
    ArrayAdapter<UserBook>(mCtx, resources, items)
{
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View
    {
        val layoutInflater : LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(resources, null)

        val name: TextView = view.findViewById(R.id.listitem_name)

        val mItem : UserBook = items[position]
        name.text = mItem.name
        return view
    }
}