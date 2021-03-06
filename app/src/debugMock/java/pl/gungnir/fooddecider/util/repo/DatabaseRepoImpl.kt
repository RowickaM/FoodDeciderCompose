package pl.gungnir.fooddecider.util.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import pl.gungnir.fooddecider.model.data.Template
import pl.gungnir.fooddecider.model.data.TemplateDetails
import pl.gungnir.fooddecider.util.*
import pl.gungnir.fooddecider.util.firebase.FirebaseAuthHelper
import pl.gungnir.fooddecider.util.firebase.FirebaseHelper

class DatabaseRepoImpl(
    private val firebaseHelper: FirebaseHelper,
    private val firebaseAuthHelper: FirebaseAuthHelper
) : DatabaseRepo {

    private val list: ArrayList<String> = arrayListOf(
        "food 1",
        "food 2",
        "food 3",
        "food 4",
        "food 5",
        "food 6"
    )

    private val userMail = "email@email.com"

    private val userPassword = "password"

    private val userUUID = "UUIDForUser"

    private val templstes: List<Template> = listOf(
        Template(
            id = "1",
            imageUrl = null,
            categoryFoodName = "category 1",
            foodCount = 0,
            foodTags = listOf(),
            foodList = listOf()
        ),
    )

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
        password: String
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

    override suspend fun splitFoodsInTemplates(template: Template): Either<Failure, Pair<TemplateDetails, List<String>>> {
        val allAddedFoods = getSavedFood()?.first() ?: return Failure.Unknown.left()
        val addedFood = arrayListOf<String>()
        val noAddedFood = arrayListOf<String>()

        template.foodList.forEach {
            if (allAddedFoods.contains(it)) {
                addedFood.add(it)
            } else {
                noAddedFood.add(it)
            }
        }

        return Pair(
            TemplateDetails(
                id = template.id,
                imageUrl = template.imageUrl,
                categoryFoodName = template.categoryFoodName,
                foodCount = template.foodCount,
                foodTags = template.foodTags,
                added = addedFood,
                notAdded = noAddedFood
            ),
            allAddedFoods
        ).right()
    }

    override suspend fun setNewFoodList(foods: List<String>): Either<Failure, None> {
        list.clear()
        list.addAll(foods)
        return None.right()
    }
}