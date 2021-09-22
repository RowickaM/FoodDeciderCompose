package pl.gungnir.fooddecider.util.config

import com.orhanobut.hawk.Hawk
import pl.gungnir.fooddecider.util.KEY_SAVED_LIST_NAME_DEFAULT
import pl.gungnir.fooddecider.util.PREF_DB_VERSION
import pl.gungnir.fooddecider.util.PREF_LIST_NAME

class ConfigImpl : Config {

    override var databaseVersion: String
        get() = Hawk.get(PREF_DB_VERSION, "1")
        set(value) {
            Hawk.put(PREF_DB_VERSION, value)
        }

    override var listName: String
        get() = Hawk.get(PREF_LIST_NAME, KEY_SAVED_LIST_NAME_DEFAULT)
        set(value) {
            Hawk.put(PREF_LIST_NAME, value)
        }
}