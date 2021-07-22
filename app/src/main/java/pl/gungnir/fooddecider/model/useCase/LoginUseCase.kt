package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class LoginUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<String, LoginUseCase.Params>() {

    data class Params(
        val email: String,
        val password: String
    )

    override suspend fun run(params: Params): Either<Failure, String> {
        return databaseRepo.loginUser(
            email = params.email,
            password = params.password
        )
    }
}