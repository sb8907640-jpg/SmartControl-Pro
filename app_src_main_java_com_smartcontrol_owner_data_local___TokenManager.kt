package com.smartcontrol.owner.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.smartcontrol.owner.util.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = Constants.PREF_NAME)

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val TOKEN = stringPreferencesKey(Constants.KEY_TOKEN)
        val USER_ID = stringPreferencesKey(Constants.KEY_USER_ID)
        val USER_EMAIL = stringPreferencesKey(Constants.KEY_USER_EMAIL)
        val USER_NAME = stringPreferencesKey(Constants.KEY_USER_NAME)
        val USER_ROLE = stringPreferencesKey(Constants.KEY_USER_ROLE)
    }

    suspend fun saveAuth(token: String, userId: String, email: String, name: String?, role: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.TOKEN] = token
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_EMAIL] = email
            prefs[Keys.USER_NAME] = name ?: ""
            prefs[Keys.USER_ROLE] = role
        }
    }

    suspend fun getToken(): String? {
        return context.dataStore.data.map { it[Keys.TOKEN] }.first()
    }

    suspend fun getUserId(): String? {
        return context.dataStore.data.map { it[Keys.USER_ID] }.first()
    }

    suspend fun getUserEmail(): String? {
        return context.dataStore.data.map { it[Keys.USER_EMAIL] }.first()
    }

    suspend fun getUserName(): String? {
        return context.dataStore.data.map { it[Keys.USER_NAME] }.first()
    }

    suspend fun getUserRole(): String? {
        return context.dataStore.data.map { it[Keys.USER_ROLE] }.first()
    }

    suspend fun isLoggedIn(): Boolean {
        return !getToken().isNullOrEmpty()
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}