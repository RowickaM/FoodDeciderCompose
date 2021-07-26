package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class LogoutUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<None, None>() {

    override suspend fun run(params: None): Either<Failure, None> {
        return databaseRepo.logoutUser()
    }
}