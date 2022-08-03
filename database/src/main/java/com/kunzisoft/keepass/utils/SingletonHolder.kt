package com.kunzisoft.keepass.utils

import com.kunzisoft.keepass.icons.InterfaceIconPackChooser

open class SingletonHolder<out T>(private val constructor: (iconPackChooser : InterfaceIconPackChooser) -> T) {

    @Volatile
    private var instance: T? = null

    fun getInstance(iconPackChooser: InterfaceIconPackChooser): T {
        return when {
            instance != null -> instance!!
            else -> synchronized(this) {
                if (instance == null) instance = constructor(iconPackChooser)
                instance!!
            }
        }
    }
}
