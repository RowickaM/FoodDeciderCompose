package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class IsUserLoggedUseCase : BaseUseCase<Boolean, None>() {

    override suspend fun run(params: None): Either<Failure, Boolean> {
        return ServiceDatabaseRepo.getDatabaseRepo().isUserLogged()
    }
}