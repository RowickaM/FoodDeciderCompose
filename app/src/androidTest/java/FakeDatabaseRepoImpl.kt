import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.repo.DatabaseRepo

class FakeDatabaseRepoImpl : DatabaseRepo {

    companion object {

        const val userMail = "email@email.com"
        const val userPassword = "password"
        const val userUUID = "UUIDForUser"

        val templstes: List<Template> = listOf(
            Template(
                id = "1",
                imageUrl = null,
                categoryFoodName = "category 1",
                foodCount = 2,
                foodTags = listOf(),
                foodList = listOf("food 1", "food 6", "food 29")
            ),
            Template(
                id = "2",
                imageUrl = null,
                categoryFoodName = "category 2",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 1")
            ),
            Template(
                id = "3",
                imageUrl = null,
                categoryFoodName = "category 3",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 1")
            ),
            Template(
                id = "4",
                imageUrl = null,
                categoryFoodName = "category 4",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 3")
            ),
            Template(
                id = "5",
                imageUrl = null,
                categoryFoodName = "category 5",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 7")
            ),
            Template(
                id = "6",
                imageUrl = null,
                categoryFoodName = "category 6",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 1")
            ),
            Template(
                id = "7",
                imageUrl = null,
                categoryFoodName = "category 7",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 1")
            ),
            Template(
                id = "8",
                imageUrl = null,
                categoryFoodName = "category 8",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 1")
            ),
            Template(
                id = "9",
                imageUrl = null,
                categoryFoodName = "category 9",
                foodCount = 1,
                foodTags = listOf(),
                foodList = listOf("food 1")
            ),
        )

        var list: List<String> = arrayListOf(
            "food 1",
            "food 2",
            "food 3",
            "food 4",
            "food 5",
            "food 6"
        )
    }

    fun changeList(list: List<String>) {
        FakeDatabaseRepoImpl.list = list
    }

    override fun getSavedFood(): Flow<List<String>>? {
        return flowOf(list)
    }

    override suspend fun loginUser(email: String, password: String): Either<Failure, String> {
        return if (email == userMail && password == userPassword) {
            userUUID.right()
        } else {
            Failure.InvalidCredentials.left()
        }
    }

    override suspend fun sendResetPasswordMail(email: String): Either<Failure, None> {
        return if (email == userMail) {
            None.right()
        } else {
            Failure.InvalidCredentials.left()
        }
    }

    override suspend fun signUpUser(
        email: String,
        password: String,
    ): Either<Failure, String> {
        return if (email != userMail) {
            userUUID.right()
        } else {
            Failure.UserCollision.left()
        }
    }

    override suspend fun createUseCollection(userUID: String): Either<Failure, None> {
        return if (userUID == userUUID) {
            None.right()
        } else {
            Failure.Unauthorized.left()
        }
    }

    override suspend fun sendVerificationEmail(userUID: String): Either<Failure, None> {
        return if (userUID == userUUID) {
            None.right()
        } else {
            Failure.Unauthorized.left()
        }
    }

    override suspend fun logoutUser(): Either<Failure, None> {
        return None.right()
    }

    override fun isUserLogged(): Either<Failure, Boolean> {
        return false.right()
    }

    override suspend fun getTemplates(): Either<Failure, List<Template>> {
        return templstes.right()
    }

    override suspend fun splitFoodsInTemplates(templateId: String): Either<Failure, TemplateDetails> {
        val template = templstes.find { it.id == templateId } ?: return Failure.Unknown.left()

        return splitToTemplateDetails(template = template).right()
    }

    override suspend fun saveNewFoodToList(item: String): Either<Failure, None> {
        val newList = ArrayList(list)
        newList.add(item)

        changeList(newList)
        return None.right()
    }

    override suspend fun setNewFoodList(foods: List<String>): Either<Failure, None> {
        changeList(foods)
        return None.right()
    }

    fun splitToTemplateDetails(template: Template): TemplateDetails {
        val allAddedFoods = list

        val addedFood = arrayListOf<String>()
        val noAddedFood = arrayListOf<String>()

        template.foodList.forEach {
            if (allAddedFoods.contains(it)) {
                addedFood.add(it)
            } else {
                noAddedFood.add(it)
            }
        }

        return TemplateDetails(
            id = template.id,
            imageUrl = template.imageUrl,
            categoryFoodName = template.categoryFoodName,
            foodCount = template.foodCount,
            foodTags = template.foodTags,
            added = addedFood,
            notAdded = noAddedFood
        )
    }
}