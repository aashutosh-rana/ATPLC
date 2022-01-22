package com.bcebhagalpur.atplc.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.adapter.StudentAdapter
import com.bcebhagalpur.atplc.model.Students
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class StudentFragment : Fragment() {

    private lateinit var etSearch: SearchView
    lateinit var adapter: StudentAdapter
    private lateinit var recyclerBachelor: RecyclerView
    lateinit var progressLayout: RelativeLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var txtItemNotFound: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_student, container, false)

        recyclerBachelor = view.findViewById(R.id.recyclerBachelor)
        etSearch=view.findViewById(R.id.etSearch)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        txtItemNotFound=view.findViewById(R.id.txtItemNotFound)
        progressLayout.visibility= View.VISIBLE

        val layoutManager = LinearLayoutManager(activity as Context)
        layoutManager.reverseLayout
        layoutManager.stackFromEnd
        recyclerBachelor.layoutManager = layoutManager
        val dividerItemDecoration =
            DividerItemDecoration(recyclerBachelor.context, layoutManager.orientation)
        recyclerBachelor.addItemDecoration(dividerItemDecoration)
        val bachelorUser = ArrayList<Students>()
        adapter = StudentAdapter(activity as Context, bachelorUser)
        recyclerBachelor.adapter = adapter
        recyclerBachelor.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        })

        getUser()
        searchCity()
        return view
    }

    private fun searchCity(){

        etSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                adapter.getFilter().filter(newText)
                return false
            }

        })
    }

    private fun getUser() {

        val child = FirebaseDatabase.getInstance().reference.child("STUDENTS")
        child.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {

            }
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = ArrayList<Students>()
                progressLayout.visibility=View.GONE

                if (snapshot.exists()) {
                    for (p0 in snapshot.children) {
                        val p1 = p0.getValue(Students::class.java)
                        userList.add(0, p1!!)
                    }
                    try {
                        adapter = StudentAdapter(activity as Context, userList)
                        if (snapshot.exists()) {
                            val maxId = snapshot.childrenCount
                            val maxInt = maxId.toInt()
                            for (i in 0..maxInt) {
                                adapter.notifyItemInserted(i)
                                recyclerBachelor.smoothScrollToPosition(i)
                            }

                        }

                    }catch (e:NullPointerException){
                        e.printStackTrace()
                    }
                    recyclerBachelor.adapter = adapter

                } else {
                    Toast.makeText(activity as Context, "Student not found", Toast.LENGTH_SHORT)
                        .show()
                    txtItemNotFound.visibility=View.VISIBLE
                }
            }

        })
    }

}
