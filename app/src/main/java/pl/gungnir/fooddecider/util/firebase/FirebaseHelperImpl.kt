package pl.gungnir.fooddecider.util.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.*

class FirebaseHelperImpl : FirebaseHelper {

    private val db by lazy { FirebaseFirestore.getInstance() }

    @ExperimentalCoroutinesApi
    override fun getSavedFoodConnection(userUID: String): Flow<List<String>> {
        return channelFlow {
            db.collection(COLLECTION_SAVED_FOOD)
                .addSnapshotListener { value, error ->
                    error?.let {
                        close()
                        return@addSnapshotListener
                    }

                    value?.let { querySnapshot ->
                        val data = querySnapshot.documents[0].data?.get(KEY_DATA_DISHES)
                        trySendBlocking(data as List<String>)
                    }
                }

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

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

                                    Template(
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
}