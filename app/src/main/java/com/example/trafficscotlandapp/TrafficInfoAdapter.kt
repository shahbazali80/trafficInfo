package com.example.trafficscotlandapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TrafficInfoAdapter(private val allRows: ArrayList<CurrentIncidents>) : RecyclerView.Adapter<TrafficInfoAdapter.ViewHolder>() {

   // private var allRows = ArrayList<CurrentIncidents>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvTitle = itemView.findViewById(R.id.tv_title) as TextView
        val tvDesc = itemView.findViewById(R.id.tv_description) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val title = allRows[position].title
        val description = allRows[position].description
        val link = allRows[position].link
        val latLng = allRows[position].latLng
        val pubDate = allRows[position].pubDate

        holder.tvTitle.text = title
        holder.tvDesc.text = description

        holder.itemView.setOnClickListener {
            //val intent = Intent(context, MainActivity::class.java)
            //intent.putExtra("isNew", "0")
            //intent.putExtra("noteTitle", title)
            //intent.putExtra("noteDescription", desc)
            //intent.putExtra("noteDate", noteDate)
            //intent.putExtra("noteId", id)
            //context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allRows.size
    }
}