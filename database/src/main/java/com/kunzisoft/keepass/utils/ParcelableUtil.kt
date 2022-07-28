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

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.LinkedHashMap

object ParcelableUtil {

    // For writing to a Parcel
    fun <K : Parcelable, V : Parcelable> writeParcelableMap(
            parcel: Parcel, flags: Int, map: Map<K, V>) {
        parcel.writeInt(map.size)
        for ((key, value) in map) {
            parcel.writeParcelable(key, flags)
            parcel.writeParcelable(value, flags)
        }
    }

    // For reading from a Parcel
    fun <K : Parcelable, V : Parcelable> readParcelableMap(
            parcel: Parcel, kClass: Class<K>, vClass: Class<V>): Map<K, V> {
        val size = parcel.readInt()
        val map = HashMap<K, V>(size)
        for (i in 0 until size) {
            val key: K? = try {
                parcel.readParcelable(kClass.classLoader)
            } catch (e: Exception) { null }
            val value: V? = try {
                parcel.readParcelable(vClass.classLoader)
            } catch (e: Exception) { null }
            if (key != null && value != null)
                map[key] = value
        }
        return map
    }

    // For writing map with string key to a Parcel
    fun <V : Parcelable> writeStringParcelableMap(
            parcel: Parcel, flags: Int, map: HashMap<String, V>) {
        parcel.writeInt(map.size)
        for ((key, value) in map) {
            parcel.writeString(key)
            parcel.writeParcelable(value, flags)
        }
    }

    // For writing map with string key and Int value to a Parcel
    fun writeStringIntMap(parcel: Parcel, map: LinkedHashMap<String, Int>) {
        parcel.writeInt(map.size)
        for ((key, value) in map) {
            parcel.writeString(key)
            parcel.writeInt(value)
        }
    }

    // For reading map with string key from a Parcel
    fun <V : Parcelable> readStringParcelableMap(
            parcel: Parcel, vClass: Class<V>): LinkedHashMap<String, V> {
        val size = parcel.readInt()
        val map = LinkedHashMap<String, V>(size)
        for (i in 0 until size) {
            val key: String? = parcel.readString()
            val value: V? = try {
                parcel.readParcelable(vClass.classLoader)
            } catch (e: Exception) { null }
            if (key != null && value != null)
                map[key] = value
        }
        return map
    }

    // For reading map with string key and Int value from a Parcel
    fun readStringIntMap(parcel: Parcel): LinkedHashMap<String, Int> {
        val size = parcel.readInt()
        val map = LinkedHashMap<String, Int>(size)
        for (i in 0 until size) {
            val key: String? = parcel.readString()
            val value: Int? = parcel.readInt()
            if (key != null && value != null)
                map[key] = value
        }
        return map
    }


    // For writing map with string key and string value to a Parcel
    fun writeStringParcelableMap(dest: Parcel, map: LinkedHashMap<String, String>) {
        dest.writeInt(map.size)
        for ((key, value) in map) {
            dest.writeString(key)
            dest.writeString(value)
        }
    }

    // For reading map with string key and string value from a Parcel
    fun readStringParcelableMap(parcel: Parcel): LinkedHashMap<String, String> {
        val size = parcel.readInt()
        val map = LinkedHashMap<String, String>(size)
        for (i in 0 until size) {
            val key: String? = parcel.readString()
            val value: String? = parcel.readString()
            if (key != null && value != null)
                map[key] = value
        }
        return map
    }
}

inline fun <reified T : Enum<T>> Parcel.readEnum() =
        readString()?.let { enumValueOf<T>(it) }

fun <T : Enum<T>> Parcel.writeEnum(value: T?) =
        writeString(value?.name)
