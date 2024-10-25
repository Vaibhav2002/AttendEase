package dev.vaibhav.attendease.shared.di

import android.content.Context
import androidx.datastore.core.DataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.vaibhav.attendease.shared.data.datastore.Preferences
import dev.vaibhav.attendease.shared.data.datastore.dataStore

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun providesFirebaseFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesPreferences(@ApplicationContext context: Context):Preferences {
        return Preferences(context.dataStore)
    }
}