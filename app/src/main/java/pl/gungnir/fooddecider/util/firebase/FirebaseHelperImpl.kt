package pl.gungnir.fooddecider.util.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.*

class FirebaseHelperImpl : FirebaseHelper {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    @ExperimentalCoroutinesApi
    override fun getSavedFoodConnection(userUID: String): Flow<List<String>> {
        return channelFlow {
            db.collection(COLLECTION_SAVED_FOOD)
                .document(userUID)
                .addSnapshotListener { value, error ->
                    error?.let {
                        close()
                        return@addSnapshotListener
                    }

                    value?.let { querySnapshot ->
                        val data = querySnapshot.data?.get(KEY_DATA_DISHES)
                        trySendBlocking(data as? List<String> ?: emptyList())
                    }
                }

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    override fun getTemplates(): Flow<List<Template>> {
        return channelFlow {
            db.collection(COLLECTION_SAVED_TEMPLATES)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = task
                            .result
                            ?.documents?.mapNotNull {
                                it.data?.let { data ->
                                    val foods = data[KEY_DATA_DISHES] as List<String>
                                    val category = data[KEY_DATA_CATEGORY] as Map<String, Any>
                                    val categoryName = category[KEY_DATA_NAME] as String
                                    val tags = category[KEY_DATA_TAGS] as List<String>
                                    val imageUrl = category[KEY_DATA_IMAGE] as String?

                                    Template(
                                        id = it.id,
                                        imageUrl = imageUrl,
                                        categoryFoodName = categoryName,
                                        foodCount = foods.size,
                                        foodTags = tags,
                                        foodList = foods
                                    )
                                }
                            }

                        trySendBlocking(list ?: emptyList())
                    }
                    close()
                }
            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    override suspend fun getSavedFood(): Flow<List<String>> {
        auth.uid?.let { uid ->
            return channelFlow {
                db.collection(COLLECTION_SAVED_FOOD)
                    .document(uid)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val data =
                                task.result?.data?.get(KEY_DATA_DISHES) ?: emptyList<String>()
                            trySendBlocking(data as? List<String> ?: emptyList())
                        }
                        close()
                    }
                awaitClose()
            }.flowOn(Dispatchers.IO)

        } ?: return flowOf(emptyList())
    }

    @ExperimentalCoroutinesApi
    override suspend fun setSavedFood(list: List<String>): Flow<Either<Failure, None>> {
        auth.uid?.let { uid ->
            return channelFlow {
                db.collection(COLLECTION_SAVED_FOOD)
                    .document(uid)
                    .set(mapOf(KEY_DATA_DISHES to list))
                    .addOnCompleteListener { task ->
                        if (task.exception != null) {
                            trySendBlocking(Failure.Unknown.left())
                        }
                        if (task.isSuccessful) {
                            trySendBlocking(None.right())
                        }
                        close()
                    }
                awaitClose()
            }.flowOn(Dispatchers.IO)
        } ?: return flowOf(Failure.Unauthorized.left())
    }

    @ExperimentalCoroutinesApi
    override fun createCollectionForUser(userUID: String): Flow<Either<Failure, None>> {
        return channelFlow {
            db.collection(COLLECTION_SAVED_FOOD)
                .document(userUID)
                .set(mapOf(KEY_DATA_DISHES to emptyList<String>()))
                .addOnCompleteListener { task ->
                    if (task.exception != null) {
                        trySendBlocking(Failure.Unknown.left())
                    }
                    if (task.isSuccessful) {
                        trySendBlocking(None.right())
                    }
                    close()
                }
            awaitClose()
        }.flowOn(Dispatchers.IO)
    }
}