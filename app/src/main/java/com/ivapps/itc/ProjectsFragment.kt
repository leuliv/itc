package com.ivapps.itc


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ivapps.itc.adapters.ProjectsHorAdapter
import com.ivapps.itc.adapters.ProjectsVerAdapter

class ProjectsFragment : Fragment() {

    var postCount = 0

    var ctx: Context? = null

    val PREF_NAME = ""
    var recentRecycler: RecyclerView? = null
    var moreRecycler: RecyclerView? = null
    var refreshLayout: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_projects, container, false)
        ctx = view.context
        recentRecycler = view.findViewById(R.id.recent_projs_recycler)
        recentRecycler!!.layoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.HORIZONTAL,
            false
        )

        moreRecycler = view.findViewById(R.id.more_projs_recycler)
        moreRecycler!!.layoutManager = LinearLayoutManager(
            view.context,
            RecyclerView.VERTICAL,
            false
        )

        recentRecycler!!.adapter = ProjectsHorAdapter(ctx!!)
        moreRecycler!!.adapter = ProjectsVerAdapter(ctx!!)


        return view
    }


}
