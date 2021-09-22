package pl.gungnir.fooddecider.util.config

import com.orhanobut.hawk.Hawk

class ConfigImpl : Config {

    override var databaseVersion: String
        get() = Hawk.get("DB_VERSION", "1")
        set(value) {
            Hawk.put("DB_VERSION", value)
        }
}