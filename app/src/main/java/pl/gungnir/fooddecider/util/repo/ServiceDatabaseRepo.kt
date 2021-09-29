package pl.gungnir.fooddecider.util.repo

import androidx.annotation.VisibleForTesting
import org.koin.java.KoinJavaComponent
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper

object ServiceDatabaseRepo {

    private val firebaseHelper by KoinJavaComponent.inject<FirebaseHelper>(FirebaseHelper::class.java)
    private val firebaseAuthHelper by KoinJavaComponent.inject<FirebaseAuthHelper>(
        FirebaseAuthHelper::class.java
    )

    @Volatile
    private var databaseRepo: DatabaseRepo? = null

    fun getDatabaseRepo(): DatabaseRepo {
        return databaseRepo ?: buildDatabaseRepo()
    }

    @VisibleForTesting
    fun changeDatabaseRepo(databaseRepo: DatabaseRepo) {
        this.databaseRepo = databaseRepo
    }

    private fun buildDatabaseRepo(): DatabaseRepo {
        return DatabaseRepoImpl(
            firebaseHelper,
            firebaseAuthHelper,
        )
    }
}