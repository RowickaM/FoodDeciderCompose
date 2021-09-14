package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class SendRemindPasswordLinkUseCase(
    private val databaseRepo: DatabaseRepo = ServiceDatabaseRepo.getDatabaseRepo(),
) : BaseUseCase<None, String>() {

    override suspend fun run(params: String): Either<Failure, None> {
        return databaseRepo.sendResetPasswordMail(email = params)
    }
}