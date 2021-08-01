package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class SignUpUserUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<String, SignUpUserUseCase.Params>() {

    override suspend fun run(params: Params): Either<Failure, String> {
        return databaseRepo.signUpUser(email = params.email, password = params.password)
    }

    class Params(
        val email: String,
        val password: String
    )
}