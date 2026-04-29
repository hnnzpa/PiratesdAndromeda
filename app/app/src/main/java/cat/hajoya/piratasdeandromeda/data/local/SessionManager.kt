package cat.hajoya.piratasdeandromeda.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.sessionDataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")

/** Persistencia de sesión con DataStore Preferences. */
class SessionManager(private val context: Context) {

    /** Flujo de id de usuario actual. */
    val userId: Flow<Int?> = context.sessionDataStore.data.map { it[KEY_USER_ID] }

    /** Flujo de nombre de usuario actual. */
    val nombreUsuario: Flow<String?> = context.sessionDataStore.data.map { it[KEY_NOMBRE_USUARIO] }

    /** Flujo con código websocket del jugador. */
    val wsCode: Flow<String?> = context.sessionDataStore.data.map { it[KEY_WS_CODE] }

    /** Flujo con código de partida. */
    val gameCode: Flow<String?> = context.sessionDataStore.data.map { it[KEY_GAME_CODE] }

    /** Guarda el id de usuario. */
    suspend fun saveUserId(value: Int) {
        context.sessionDataStore.edit { prefs -> prefs[KEY_USER_ID] = value }
    }

    /** Guarda nombre de usuario. */
    suspend fun saveNombreUsuario(value: String) {
        context.sessionDataStore.edit { prefs -> prefs[KEY_NOMBRE_USUARIO] = value }
    }

    /** Guarda ws_code de sesión. */
    suspend fun saveWsCode(value: String) {
        context.sessionDataStore.edit { prefs -> prefs[KEY_WS_CODE] = value }
    }

    /** Guarda código de partida de sesión. */
    suspend fun saveGameCode(value: String) {
        context.sessionDataStore.edit { prefs -> prefs[KEY_GAME_CODE] = value }
    }

    /** Limpia toda la sesión persistida. */
    suspend fun clearSession() {
        context.sessionDataStore.edit { it.clear() }
    }

    companion object {
        val KEY_USER_ID = intPreferencesKey("key_user_id")
        val KEY_NOMBRE_USUARIO = stringPreferencesKey("key_nombre_usuario")
        val KEY_WS_CODE = stringPreferencesKey("key_ws_code")
        val KEY_GAME_CODE = stringPreferencesKey("key_game_code")
    }
}

