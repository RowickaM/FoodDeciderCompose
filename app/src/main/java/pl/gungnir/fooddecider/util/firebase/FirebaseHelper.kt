package pl.gungnir.fooddecider.util.firebase

import kotlinx.coroutines.flow.Flow
import pl.gungnir.fooddecider.model.data.SavedFoodCollection
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None

interface FirebaseHelper {

    fun getSavedFoodConnection(userUID: String, listName: String): Flow<SavedFoodCollection>

    fun getTemplates(): Flow<List<Template>>

    fun getTemplate(id: String): Flow<Template?>

    suspend fun getSavedFood(listName: String): Flow<List<String>>

    suspend fun setSavedFood(listName: String, list: List<String>): Flow<Either<Failure, None>>

    fun createCollectionForUser(userUID: String): Flow<Either<Failure, None>>

    fun getActualDatabaseVersion(): Flow<Either<Failure, String>>

    fun saveInNewStructure(uid: String, oldList: List<String>): Flow<Either<Failure.Unknown, None>>

    fun updateStructure(uid: String): Flow<Either<Failure.Unknown, None>>

    fun addNewList(uid: String, listName: String): Flow<Either<Failure, None>>

    fun getListsName(uid: String): Flow<Either<Failure, List<String>>>
}