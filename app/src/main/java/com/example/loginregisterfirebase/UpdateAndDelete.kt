package com.example.loginregisterfirebase

interface UpdateAndDelete{
    fun modifyItem(itemUID:String , isDone:Boolean)
    fun onItemDelete(itemUID: String)
}