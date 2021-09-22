package pl.gungnir.fooddecider.model.useCase

import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.util.Either
import pl.gungnir.fooddecider.util.Failure
import pl.gungnir.fooddecider.util.None
import pl.gungnir.fooddecider.util.repo.DatabaseRepo
import pl.gungnir.fooddecider.util.repo.ServiceDatabaseRepo

class GetTemplatesUseCase(
    private val databaseRepo: DatabaseRepo = ServiceDatabaseRepo.getDatabaseRepo()
) : BaseUseCase<List<Template>, None>() {

    override suspend fun run(params: None): Either<Failure, List<Template>> {
        return databaseRepo.getTemplates()
    }
}