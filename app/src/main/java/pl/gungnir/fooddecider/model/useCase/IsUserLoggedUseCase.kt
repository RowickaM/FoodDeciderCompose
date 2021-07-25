package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class IsUserLoggedUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<Boolean, None>() {

    override suspend fun run(params: None): Either<Failure, Boolean> {
        return databaseRepo.isUserLogged()
    }
}