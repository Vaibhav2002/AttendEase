package dev.vaibhav.attendease.student

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.vaibhav.attendease.shared.FirebaseKt

@HiltAndroidApp
class AttendEaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseKt.init(this)
    }
}