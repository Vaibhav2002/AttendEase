package dev.vaibhav.attendease.shared.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

class Preferences(
    dataStore:DataStore<Preferences>
) {
    var user by PreferenceDataStore(
        dataStore = dataStore,
        key = PrefKeys.USER,
        defaultValue = String()
    )
}