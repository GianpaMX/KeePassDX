/*
 * Copyright 2019 Jeremy Jamet / Kunzisoft.
 *
 * This file is part of KeePassDX.
 *
 *  KeePassDX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDX.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
