package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class LoginUseCase : BaseUseCase<String, LoginUseCase.Params>() {

    data class Params(
        val email: String,
        val password: String,
    )

    override suspend fun run(params: Params): Either<Failure, String> {
        return ServiceDatabaseRepo.getDatabaseRepo().loginUser(
            email = params.email,
            password = params.password
        )
    }
}