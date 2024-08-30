package dev.vaibhav.attendease.shared

import android.content.Context
import com.google.firebase.FirebaseApp

object FirebaseKt {
    fun init(context: Context){
        FirebaseApp.initializeApp(context)
    }
}