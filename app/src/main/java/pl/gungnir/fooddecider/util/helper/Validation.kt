package pl.gungnir.fooddecider.util.helper

import java.util.regex.Pattern

fun isEmailValid(email: String): Boolean {
    return Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    ).matcher(email).matches()
}

fun isPasswordValid(password: String): Boolean {
    return password.length >= 6
}