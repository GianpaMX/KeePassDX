/*
 * Copyright 2018 Jeremy Jamet / Kunzisoft.
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
package com.kunzisoft.keepass.icons

import android.content.Context
import android.util.Log
import com.kunzisoft.keepass.BuildConfig
import com.kunzisoft.keepass.R
import com.kunzisoft.keepass.settings.DatabasePreferencesUtil
import java.util.ArrayList

/**
 * Utility class to built and select an IconPack dynamically by libraries importation
 *
 * @author J-Jamet
 */
object IconPackChooser : InterfaceIconPackChooser {

    private val TAG = IconPackChooser::class.java.name

    private val iconPackList = ArrayList<IconPack>()
    private var iconPackSelected: IconPack? = null
    private var defaultIconSize: Int? = null

    private var isIconPackChooserBuilt: Boolean = false

    /**
     * Built the icon pack chooser based on imports made in *build.gradle*
     *
     *
     * Dynamic import can be done for each flavor by prefixing the 'implementation' command with the name of the flavor.< br/>
     * (ex : `libreImplementation project(path: ':icon-pack-classic')` <br></br>
     * Each name of icon pack must be in `ICON_PACKS` in the build.gradle file
     *
     * @param context Context to construct each pack with the resources
     * @return An unique instance of [IconPackChooser], recall [.build] provide the same instance
     */
    override fun build(context: Context) {
        synchronized(IconPackChooser::class.java) {
            if (!isIconPackChooserBuilt) {
                isIconPackChooserBuilt = true

                for (iconPackString in BuildConfig.ICON_PACKS) {
                    addOrCatchNewIconPack(context, iconPackString)
                }
                if (iconPackList.isEmpty()) {
                    Log.e(TAG, "Icon packs can't be load, retry with one by default")
                    addDefaultIconPack(context)
                }
                if(defaultIconSize == null) {
                    setDefaultIconSize(context.resources.getDimension(R.dimen.icon_size).toInt())
                }
            }
        }
    }

    /**
     * Construct dynamically the icon pack provide by the default string resource "resource_id"
     */
    override fun addDefaultIconPack(context: Context) {
        val resourceId = context.resources.getIdentifier("resource_id", "string", context.packageName)
        iconPackList.add(IconPack(context.packageName, context.resources, resourceId))
    }

    /**
     * Utility method to add new icon pack or catch exception if not retrieve
     */
    override fun addOrCatchNewIconPack(context: Context, iconPackString: String) {
        try {
            iconPackList.add(IconPack(context.packageName,
                context.resources,
                context.resources.getIdentifier(
                    iconPackString + "_resource_id",
                    "string",
                    context.packageName)))
        } catch (e: Exception) {
            Log.w(TAG, "Icon pack $iconPackString can't be load")
        }

    }

    override fun setSelectedIconPack(iconPackIdString: String?) {
        for (iconPack in iconPackList) {
            if (iconPack.id == iconPackIdString) {
                iconPackSelected = iconPack
                break
            }
        }
    }

    /**
     * Get the current IconPack used
     *
     * @param context Context to build the icon pack if not already build
     * @return IconPack currently in usage
     */
    override fun getSelectedIconPack(context: Context): IconPack? {
        build(context)
        if (iconPackSelected == null) {
            setSelectedIconPack(DatabasePreferencesUtil.getIconPackSelectedId(context))
        }
        return iconPackSelected
    }

    /**
     * Get the list of IconPack available
     *
     * @param context Context to build the icon pack if not already build
     * @return IconPack available
     */
    override fun getIconPackList(context: Context): List<IconPack> {
        build(context)
        return iconPackList
    }

    override fun setDefaultIconSize(size: Int) {
        defaultIconSize = size
    }

    override fun getDefaultIconSize(): Int {
        return defaultIconSize!!
    }
}
