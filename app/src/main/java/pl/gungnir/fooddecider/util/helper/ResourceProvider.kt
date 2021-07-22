package pl.gungnir.fooddecider.util.helper

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes

interface ResourceProvider {

    fun getString(@StringRes stringId: Int): String
    fun getString(@StringRes stringId: Int, param: String): String
    fun getString(@StringRes stringId: Int, vararg params: Any): String
    fun getPlurals(@PluralsRes pluralsId: Int, params: Int): String
}