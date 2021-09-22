package pl.gungnir.fooddecider.util.repo

import androidx.annotation.VisibleForTesting
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelperImpl
import pl.gungnir.fooddecider.util.firebase.FirebaseHelperImpl

object ServiceDatabaseRepo {

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
            FirebaseHelperImpl(),
            FirebaseAuthHelperImpl()
        )
    }

}