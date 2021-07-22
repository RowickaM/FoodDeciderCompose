package pl.gungnir.fooddecider.util.helper

import android.content.Context
import androidx.annotation.PluralsRes

class ResourceProviderImpl(val context: Context) : ResourceProvider {

    override fun getString(stringId: Int): String = context.getString(stringId)

    override fun getString(stringId: Int, param: String): String =
        context.getString(stringId, param)

    override fun getString(stringId: Int, vararg params: Any): String =
        context.getString(stringId, *params)

    override fun getPlurals(@PluralsRes pluralsId: Int, params: Int): String =
        context.resources.getQuantityString(pluralsId, params, params)
}