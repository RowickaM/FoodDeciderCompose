package pl.gungnir.fooddecider.util

sealed class Failure {
    object Unknown : Failure()
    object FirebaseError : Failure()
}
