package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class GetTemplatesUseCase(
    private val databaseRepo: DatabaseRepo
) : BaseUseCase<List<Template>, None>() {

    override suspend fun run(params: None): Either<Failure, List<Template>> {
        return databaseRepo.getTemplates()
    }
}