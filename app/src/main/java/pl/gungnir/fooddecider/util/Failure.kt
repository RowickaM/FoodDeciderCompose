package pl.gungnir.fooddecider.util

sealed class Failure {
    object Unknown : Failure()
    object UserNotExist : Failure()
    object InvalidCredentials : Failure()
    object UserCollision : Failure()
    object FirebaseAuthUnknown : Failure()
    object Unauthorized : Failure()
}