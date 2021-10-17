package pl.gungnir.fooddecider.util.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.*
import pl.gungnir.fooddecider.model.data.SavedFoodCollection
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.*

class FirebaseHelperImpl : FirebaseHelper {

    private val db by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    @ExperimentalCoroutinesApi
    override fun getSavedFoodConnection(
        userUID: String,
        listName: String
    ): Flow<SavedFoodCollection> {
        return channelFlow {
            db.collection(COLLECTION_SAVED_FOOD)
                .document(userUID)
                .addSnapshotListener { value, error ->
                    error?.let {
                        close()
                        return@addSnapshotListener
                    }

                    value?.let { querySnapshot ->

                        querySnapshot.data?.let { data ->
                            val arrays = data[KEY_SAVED_LIST] as Map<String, Any>
                            val lists = arrays[listName] as? Map<String, Any?>
                            val items = lists?.get(KEY_SAVED_ITEM) as? List<String>

                            trySendBlocking(
                                SavedFoodCollection(
                                    allListName = arrays.keys.toList(),
                                    selectedListName = listName,
                                    savedList = items ?: emptyList()
                                )
                            )
                        }
                    }
                }

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    override fun addNewList(uid: String, listName: String): Flow<Either<Failure, None>> {
        return channelFlow {
            db.collection(COLLECTION_SAVED_FOOD)
                .document(uid)
                .update(
                    mapOf("$KEY_SAVED_LIST.$listName.$KEY_SAVED_ITEM" to emptyList<String>())
                )
                .addOnCompleteListener { task ->
                    if (task.exception != null) {
                        trySendBlocking(Failure.Unknown.left())
                    } else if (task.isSuccessful) {
                        trySendBlocking(None.right())
                    }
                    close()
                }
            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    override fun getListsName(uid: String): Flow<Either<Failure, List<String>>> {
        return channelFlow {
            db.collection(COLLECTION_SAVED_FOOD)
                .document(uid)
                .addSnapshotListener { value, error ->
                    error?.let {
                        trySendBlocking(Failure.Unknown.left())
                        return@addSnapshotListener
                    }

                    value?.let { querySnapshot ->

                        querySnapshot.data?.let { data ->
                            val arrays = data[KEY_SAVED_LIST] as Map<String, Any>
                            val lists = arrays.keys.toList()
                            trySendBlocking(lists.right())
                        } ?: trySendBlocking(Failure.Unknown.left())
                    } ?: trySendBlocking(Failure.Unknown.left())
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
    override fun getTemplate(id: String): Flow<Template?> = channelFlow {
        db.collection(COLLECTION_SAVED_TEMPLATES)
            .document(id)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val template = task
                        .result
                        ?.data?.let { data ->
                            val foods = data[KEY_DATA_DISHES] as List<String>
                            val category = data[KEY_DATA_CATEGORY] as Map<String, Any>
                            val categoryName = category[KEY_DATA_NAME] as String
                            val tags = category[KEY_DATA_TAGS] as List<String>
                            val imageUrl = category[KEY_DATA_IMAGE] as String?

                            Template(
                                id = id,
                                imageUrl = imageUrl,
                                categoryFoodName = categoryName,
                                foodCount = foods.size,
                                foodTags = tags,
                                foodList = foods
                            )
                        }

                    trySendBlocking(template)
                }
                close()
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    override suspend fun getSavedFood(
        listName: String
    ): Flow<List<String>> {
        auth.uid?.let { uid ->
            return channelFlow {
                db.collection(COLLECTION_SAVED_FOOD)
                    .document(uid)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val savedItem = task.result?.data?.let { data ->
                                val arrays = data[KEY_SAVED_LIST] as Map<String, Any>
                                val lists = arrays[listName] as? Map<String, Any?>
                                lists?.get(KEY_SAVED_ITEM) as? List<String>
                            } ?: emptyList()


                            trySendBlocking(savedItem as? List<String> ?: emptyList())
                        }
                        close()
                    }
                awaitClose()
            }.flowOn(Dispatchers.IO)

        } ?: return flowOf(emptyList())
    }

    @ExperimentalCoroutinesApi
    override suspend fun setSavedFood(
        listName: String,
        list: List<String>
    ): Flow<Either<Failure, None>> {
        auth.uid?.let { uid ->
            return channelFlow {
                db.collection(COLLECTION_SAVED_FOOD)
                    .document(uid)
                    .update(
                        mapOf(
                            "$KEY_SAVED_LIST.$listName.$KEY_SAVED_ITEM" to list
                        )
                    )
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
    override fun getActualDatabaseVersion(): Flow<Either<Failure, String>> = channelFlow {
        db.collection(COLLECTION_SETUP)
            .document(KEY_SETUP_DB)
            .get()
            .addOnCompleteListener { task ->
                if (task.exception != null) {
                    trySendBlocking(Failure.Unknown.left())
                }
                if (task.isSuccessful) {
                    val version = task.result?.data?.get(KEY_SETUP_DB_VERSION) as String
                    trySendBlocking(version.right())
                }
                close()
            }
        awaitClose()
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    override fun createCollectionForUser(userUID: String): Flow<Either<Failure, None>> {
        return saveInNewStructure(userUID, emptyList())
    }

    @ExperimentalCoroutinesApi
    override fun saveInNewStructure(uid: String, oldList: List<String>) = channelFlow {
        db.collection(COLLECTION_SAVED_FOOD)
            .document(uid)
            .set(
                mapOf<String, Any>(
                    KEY_SAVED_LIST to mapOf<String, Any>(
                        KEY_SAVED_LIST_NAME_DEFAULT to mapOf(
                            KEY_SAVED_ITEM to oldList
                        )
                    )
                )
            )
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

    @ExperimentalCoroutinesApi
    override fun updateStructure(uid: String) = channelFlow {
        val oldList = getOldSavedFood().first()

        db.collection(COLLECTION_SAVED_FOOD)
            .document(uid)
            .set(
                mapOf<String, Any>(
                    KEY_SAVED_LIST to mapOf<String, Any>(
                        KEY_SAVED_LIST_NAME_DEFAULT to mapOf(
                            KEY_SAVED_ITEM to oldList
                        )
                    )
                )
            )
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

    private suspend fun getOldSavedFood(): Flow<List<String>> {
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
}