package com.bcebhagalpur.atplc.adapter

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.activity.DetailActivity
import com.bcebhagalpur.atplc.model.Students
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList


class StudentAdapter(val context: Context, private val studentUser: ArrayList<Students>) : RecyclerView.Adapter<StudentAdapter.MyViewHolder>()  {

    var studentFilter = ArrayList<Students>()
    init {
        studentFilter = studentUser
    }

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var image: ImageView = itemView.findViewById(R.id.img)
        var txtName: TextView = itemView.findViewById(R.id.txtName)
        var txtCollege: TextView = itemView.findViewById(R.id.txtCollege)
        var txtBranch: TextView = itemView.findViewById(R.id.txtBranch)
        var btnSendEmail: Button =itemView.findViewById(R.id.btnSendEmail)
        var description:CardView=itemView.findViewById(R.id.description)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView= LayoutInflater.from(context).inflate(R.layout.recycler_student, parent, false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return studentFilter.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val student=studentFilter[position]
        holder.txtName.text=student.fullName
        holder.txtBranch.text=student.branch
        holder.txtCollege.text=student.college

        val imageView=holder.image
        val picasso= Picasso.get().load(student.image).fit()
        picasso.error(R.drawable.atplc).into(imageView)

        holder.btnSendEmail.setOnClickListener {
            sendEmail(student.email.toString())
        }

        holder.description.setOnClickListener {
            val intent= Intent(context, DetailActivity::class.java)
            intent.putExtra("imageUrl", student.image)
            intent.putExtra("college", student.college)
            intent.putExtra("mobileNumber", student.mobileNumber)
            intent.putExtra("branch", student.branch)
            intent.putExtra("fullName", student.fullName)
            intent.putExtra("userId", student.userId)
            intent.putExtra("email", student.email)
            context.startActivity(intent)

        }
    }

    fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val charSearch = constraint.toString()
                studentFilter = if (charSearch.isEmpty()) {
                    studentUser
                } else {
                    val resultList = ArrayList<Students>()
                    for (row in studentUser) {
                        if (row.fullName!!.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            ) || row.college!!.toLowerCase(Locale.ROOT).contains(
                                charSearch
                                    .toLowerCase(Locale.ROOT)
                            ) || row.branch!!.toLowerCase(Locale.ROOT).contains(
                                charSearch.toLowerCase(
                                    Locale.ROOT
                                )
                            )) {
                            resultList.add(row)
                        }
                    }
                    resultList
                }
                val filterResults = FilterResults()
                filterResults.values = studentFilter
                return filterResults

            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                studentFilter = results!!.values as java.util.ArrayList<Students>
                notifyDataSetChanged()

            }

        }
    }

    private fun sendEmail(email: String){
        try {
        val inflateView=LayoutInflater.from(context).inflate(R.layout.custom_view, null)
        val etSendMessage=inflateView.findViewById<EditText>(R.id.etEmailMessage)
        val alertDialog= AlertDialog.Builder(context)
        alertDialog.setTitle("write massage")
        alertDialog.setView(inflateView)
        alertDialog.setCancelable(false)
        alertDialog.setNegativeButton("Cancel"){ dialog, which ->
            Toast.makeText(context, "you cancel the massage", Toast.LENGTH_SHORT).show()
        }
        alertDialog.setPositiveButton("Send"){ dialog, _ ->
            try {
                val intent = Intent(Intent.ACTION_SEND)
                val strTo = arrayOf(email)
                intent.putExtra(Intent.EXTRA_EMAIL, strTo)
                intent.putExtra(Intent.EXTRA_SUBJECT, "ATPLC")
                intent.putExtra(Intent.EXTRA_TEXT, etSendMessage.text.toString())
                intent.type = "message/rfc822"
                intent.setPackage("com.google.android.gm")
                try {
                    context.startActivity(intent)
                }catch (e:ActivityNotFoundException){
                    e.printStackTrace()
                }
            }catch (e:java.lang.Exception){
                e.printStackTrace()
            }


        }
        val dialog=alertDialog.create()
        dialog.show()
    }catch (e: Exception){
            e.printStackTrace()
        }
    }

}