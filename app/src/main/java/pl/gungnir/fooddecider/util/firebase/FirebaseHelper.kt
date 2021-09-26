package pl.gungnir.fooddecider.util.firebase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None

interface FirebaseHelper {

    fun getSavedFoodConnection(userUID: String, listName: String): Flow<List<String>>

    fun getTemplates(): Flow<List<Template>>

    fun getTemplate(id: String): Flow<Template?>

    suspend fun getSavedFood(): Flow<List<String>>

    suspend fun setSavedFood(list: List<String>): Flow<Either<Failure, None>>

    fun createCollectionForUser(userUID: String): Flow<Either<Failure, None>>

    fun getActualDatabaseVersion(): Flow<Either<Failure, String>>

    fun saveInNewStructure(uid: String, oldList: List<String>): Flow<Either<Failure.Unknown, None>>

    fun updateStructure(uid: String): Flow<Either<Failure.Unknown, None>>
}