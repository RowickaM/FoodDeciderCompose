package pl.gungnir.fooddecider.util.firebase

import kotlinx.coroutines.flow.Flow

interface FirebaseHelper {

    fun getSavedFoodConnection(userUID: String): Flow<List<String>>

    fun getTemplates()
}