package com.bcebhagalpur.atplc.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.activity.HomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FeedbackFragment : Fragment() {

    private lateinit var etF1:EditText
    private lateinit var etF2:EditText
    private lateinit var etF3:EditText
    private lateinit var etF4: EditText
    private lateinit var ratingBar: RatingBar
    private lateinit var txtRating: TextView
    private lateinit var btnSendFeedback: Button
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDatabase: FirebaseDatabase
    private lateinit var mAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_feedback, container, false)

        etF1=view.findViewById(R.id.etF1)
        etF2=view.findViewById(R.id.etF2)
        etF3=view.findViewById(R.id.etF3)
        etF4=view.findViewById(R.id.etF4)
        ratingBar=view.findViewById(R.id.ratingBar)
        txtRating=view.findViewById(R.id.txtRate)
        btnSendFeedback=view.findViewById(R.id.btnSendFeedback)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase.reference.child("FEEDBACK")


        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            txtRating.text = rating.toString()
        }

        btnSendFeedback.setOnClickListener {
            sendFeedback()
        }

        return view
    }

    private fun sendFeedback(){
        val fa=etF1.text.toString()
        val fb=etF2.text.toString()
        val fc=etF3.text.toString()
        val fd=etF4.text.toString()
        val ratingValue= ratingBar.rating.toString()
        val userId= mAuth.currentUser!!.uid
        val query=mDatabaseReference.child(userId)
        val child = FirebaseDatabase.getInstance().reference.child("STUDENTS")
        val anotherChild1=child.child(userId)
        anotherChild1.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val name = snapshot.child("fullName").value.toString()
                    val number=snapshot.child("mobileNumber").value.toString()
                    val college=snapshot.child("college").value.toString()

                        query.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val maxId = snapshot.childrenCount
                                val anotherChild = query.child((maxId + 1).toString())
                                anotherChild.child("id").setValue((maxId + 1).toString())
                                anotherChild.child("f1").setValue(fa)
                                anotherChild.child("f2").setValue(fb)
                                anotherChild.child("f3").setValue(fc)
                                anotherChild.child("f4").setValue(fd)
                                anotherChild.child("rating").setValue(ratingValue)
                                anotherChild.child("fullName").setValue(name)
                                anotherChild.child("college").setValue(college)
                                anotherChild.child("mobileNumber").setValue(number)
                                Toast.makeText(activity as Context,"Thank you to giv us your time",Toast.LENGTH_SHORT).show()
                                startActivity(Intent(activity as Context,HomeActivity::class.java))
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(
                                    activity,
                                    "some error occurred!! please try again or contact to developer",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "name not found", Toast.LENGTH_SHORT).show()
            }

        })

    }

}
