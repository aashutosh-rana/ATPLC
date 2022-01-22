package com.bcebhagalpur.atplc.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bcebhagalpur.atplc.R
import com.bcebhagalpur.atplc.activity.LoginActivity
import com.bcebhagalpur.atplc.model.ChatQna
import com.bcebhagalpur.atplc.model.Students
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.lang.NullPointerException
import java.util.*
import kotlin.collections.ArrayList

class ChatAdapter(val context: Context, private val chatList: ArrayList<ChatQna>) : RecyclerView.Adapter<ChatAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var cardView:CardView=itemView.findViewById(R.id.cardView)
        var txtChat:TextView=itemView.findViewById(R.id.txtChat)
        var txtName:TextView=itemView.findViewById(R.id.txtName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatAdapter.MyViewHolder {
        val itemView= LayoutInflater.from(context).inflate(R.layout.qna_item,parent,false)
        return MyViewHolder(itemView)
    }
    override fun getItemCount(): Int {
        return chatList.size
    }
    override fun onBindViewHolder(holder: ChatAdapter.MyViewHolder, position: Int) {
        val chat = chatList[position]
        holder.txtChat.text=chat.message
        holder.txtName.text=chat.fullName
        holder.cardView.setOnClickListener {
            if (FirebaseAuth.getInstance().currentUser!!.uid=="Mmy9R8RrhNgYoatte94hnep0DZo2")
            {
                val mAlertDialogBuilder = AlertDialog.Builder(context)
                mAlertDialogBuilder.setTitle("Message")
                mAlertDialogBuilder.setMessage("Delete this message")
                mAlertDialogBuilder.setCancelable(false)
                mAlertDialogBuilder.setPositiveButton("Ok"){_,_->
                    val myDatabase= FirebaseDatabase.getInstance().getReference("CHAT")
                    try {
                        val myDatabase3 = myDatabase.child(chat.id.toString())
                        myDatabase3.removeValue()
                    }catch (e: NullPointerException){
                        e.printStackTrace()
                    }
                }
                mAlertDialogBuilder.setNegativeButton("cancel"){_,_->
                    Toast.makeText(context,"You cancel the operation", Toast.LENGTH_SHORT).show()
                }
                mAlertDialogBuilder.create().show()
            }
        }
    }
}