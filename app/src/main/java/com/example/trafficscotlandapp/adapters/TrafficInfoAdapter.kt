package com.example.trafficscotlandapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.trafficscotlandapp.R
import com.example.trafficscotlandapp.models.CurrentIncidents
import com.example.trafficscotlandapp.ui.DetailActivity

class TrafficInfoAdapter(private val allRows: ArrayList<CurrentIncidents>) : RecyclerView.Adapter<TrafficInfoAdapter.ViewHolder>() {

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
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("description", description)
            intent.putExtra("link", link)
            intent.putExtra("latLng", latLng)
            intent.putExtra("pubDate", pubDate)
            it.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return allRows.size
    }
}