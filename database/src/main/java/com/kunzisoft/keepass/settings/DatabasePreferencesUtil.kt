package com.kunzisoft.keepass.settings

import android.content.Context
import android.preference.PreferenceManager
import com.kunzisoft.keepass.database.R
import com.kunzisoft.keepass.timeout.TimeoutHelper

object DatabasePreferencesUtil {

    fun showExpiredEntries(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return ! prefs.getBoolean(context.getString(R.string.hide_expired_entries_key),
            context.resources.getBoolean(R.bool.hide_expired_entries_default))
    }

    fun getIconPackSelectedId(context: Context): String? {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getString(
            context.getString(R.string.setting_icon_pack_choose_key),
            context.getString(R.string.setting_icon_pack_choose_default))
    }

    fun searchSubdomains(context: Context): Boolean {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getBoolean(context.getString(R.string.subdomain_search_key),
            context.resources.getBoolean(R.bool.subdomain_search_default))
    }

    /**
     * Save current time, can be retrieve with `getTimeSaved()`
     */
    fun saveCurrentTime(context: Context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
            putLong(context.getString(R.string.timeout_backup_key), System.currentTimeMillis())
            apply()
        }
    }

    /**
     * Time previously saved in milliseconds (commonly used to compare with current time and check timeout)
     */
    fun getTimeSaved(context: Context): Long {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        return prefs.getLong(context.getString(R.string.timeout_backup_key),
            TimeoutHelper.NEVER)
    }

    /**
     * App timeout selected in milliseconds
     */
    fun getAppTimeout(context: Context): Long {
        return try {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            (prefs.getString(context.getString(R.string.app_timeout_key),
                context.getString(R.string.timeout_default)) ?: "300000").toLong()
        } catch (e: NumberFormatException) {
            TimeoutHelper.DEFAULT_TIMEOUT
        }
    }
}
