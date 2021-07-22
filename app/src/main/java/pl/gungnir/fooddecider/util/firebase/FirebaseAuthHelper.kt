package pl.gungnir.fooddecider.util.firebase

import kotlinx.coroutines.flow.Flow

interface FirebaseAuthHelper {

    fun login(email: String, password: String): Flow<String>

    fun userIsLogged(): Boolean

    fun getUID(): String
}