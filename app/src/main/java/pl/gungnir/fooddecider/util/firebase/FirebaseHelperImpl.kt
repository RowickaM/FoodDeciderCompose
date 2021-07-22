package pl.gungnir.fooddecider.util.firebase

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import pl.gungnir.fooddecider.util.COLLECTION_SAVED_FOOD

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
                        val data = querySnapshot.documents[0].data?.get("dishes")
                        trySendBlocking(data as List<String>)
                    }
                }

            awaitClose()
        }.flowOn(Dispatchers.IO)
    }

    override fun getTemplates() {
        TODO("Not yet implemented")
    }
}