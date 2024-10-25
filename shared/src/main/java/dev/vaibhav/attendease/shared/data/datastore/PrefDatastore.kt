package dev.vaibhav.attendease.shared.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

internal class PreferenceDataStore(
    private val dataStore: DataStore<Preferences>,
    private val key: Preferences.Key<String>,
    private val defaultValue: String,
) : ReadWriteProperty<Any, String> {

    override fun getValue(thisRef: Any, property: KProperty<*>) = runBlocking(Dispatchers.IO) {
        dataStore.data.map { it[key] ?: defaultValue }.first()
    }


    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: String
    ): Unit = runBlocking(Dispatchers.IO) {
        dataStore.edit { it[key] = value }
    }
}