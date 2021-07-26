package pl.gungnir.fooddecider.util.firebase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.model.data.Template

interface FirebaseHelper {

    fun getSavedFoodConnection(userUID: String): Flow<List<String>>

    fun getTemplates(): Flow<List<Template>>

    suspend fun getSavedFood(): Flow<List<String>>
}