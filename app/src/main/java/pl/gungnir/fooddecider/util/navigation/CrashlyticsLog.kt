package pl.gungnir.fooddecider.util.navigation

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsLog {

    fun log(logMsg: String) {
        FirebaseCrashlytics.getInstance().log(logMsg)
    }
}

const val NAV_LOGIN_LOG = "navigation_to_login_screen"
const val NAV_REGISTRATION_LOG = "navigation_to_registration"
const val NAV_FORGOT_PASSWORD_LOG = "navigation_to_forgot_password"
const val NAV_RANDOM_LOG = "navigation_to_random"
const val NAV_RANDOM_LIST_LOG = "navigation_to_random_list"
const val NAV_FOOD_TEMPLATE_LOG = "navigation_to_templates"
const val NAV_FOOD_TEMPLATE_DETAILS_LOG = "navigation_to_template_details"