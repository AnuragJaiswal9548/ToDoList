package com.example.loginregisterfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity(),UpdateAndDelete {



    lateinit var database:DatabaseReference
    lateinit var adapter:ToDoAdapter
    private lateinit var auth: FirebaseAuth
    var toDOList:MutableList<ToDoModel>?=null
    private var listViewItem: ListView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title="Welcome"
        auth = FirebaseAuth.getInstance()



        val fab= findViewById<View>(R.id.fab) as FloatingActionButton
        database=FirebaseDatabase.getInstance().reference

        listViewItem= findViewById<ListView>(R.id.item_listView)


        fab.setOnClickListener { view->
            val alertDialog=AlertDialog.Builder(this)
            val textEditText=EditText(this)
            alertDialog.setMessage("Add todo item")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add"){dialog,i->
                val todoItemData=ToDoModel.createList()
                todoItemData.itemDataText=textEditText.text.toString()
                todoItemData.done=false
                val newItemData=database.child("todo").push()
                todoItemData.UID=newItemData.key
                newItemData.setValue(todoItemData)
                dialog.dismiss()
                Toast.makeText(this,"item saved",Toast.LENGTH_LONG).show()

            }
            alertDialog.show()

        }
        toDOList= mutableListOf<ToDoModel>()
       adapter= ToDoAdapter(this,toDOList!!)
        listViewItem!!.adapter=adapter
        database.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,"No item Added",Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                toDOList!!.clear()
                addItemToList(snapshot)
            }
        })

    }

    @SuppressLint("SuspiciousIndentation")
    private fun addItemToList(snapshot: DataSnapshot) {
    val items=snapshot.children.iterator()
        if(items.hasNext()){
            val toDoIndexedValue=items.next();
            val itemsIterator=toDoIndexedValue.children.iterator()

            while(itemsIterator.hasNext()){
                val currentItem=itemsIterator.next()
                val toDoItemData=ToDoModel.createList()
                val map=currentItem.getValue() as HashMap<String,Any>
                toDoItemData.UID=currentItem.key
                toDoItemData.done=map.get("done") as Boolean?
                toDoItemData.itemDataText=map.get("itemDataText") as String?
                toDOList!!.add(toDoItemData)
            }
        }
        adapter.notifyDataSetChanged()
    }


    fun signOut(view: View) {
        auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference=database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference=database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }


}