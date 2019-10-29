package com.ivapps.itc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivapps.itc.R

class ProjectsVerAdapter internal constructor(
    private val context: Context
) :
    RecyclerView.Adapter<ProjectsVerAdapter.ViewHolder>() {

    private val mInflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, p: Int): ViewHolder {
        val view = mInflater.inflate(R.layout.single_ver_project, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return 10
    }

    override fun onBindViewHolder(holder: ViewHolder, p: Int) {
        //val post = list[p]


        //holder.titleView.text = post.post_title

        holder.itemView.setOnClickListener{

        }
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //val titleView: TextView = itemView.findViewById(R.id.post_title)
    }


}