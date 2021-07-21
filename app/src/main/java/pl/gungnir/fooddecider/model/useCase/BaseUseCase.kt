package pl.gungnir.fooddecider.model.useCase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure

abstract class BaseUseCase<out Type, in Params> where Type : Any {

    abstract suspend fun run(params: Params): Either<Failure, Type>

    suspend operator fun invoke(
        params: Params,
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        onResult: (Either<Failure, Type>) -> Unit = {}
    ) {
        val result = withContext(dispatcher) {
            run(params)
        }
        onResult(result)

    }
}