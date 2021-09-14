import org.junit.Before
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

open class BaseTest {

    val databaseRepo = FakeDatabaseRepoImpl()

    @Before
    open fun setup() {
        ServiceDatabaseRepo.changeDatabaseRepo(databaseRepo)
    }
}