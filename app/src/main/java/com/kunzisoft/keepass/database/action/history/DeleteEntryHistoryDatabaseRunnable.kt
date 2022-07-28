package com.kunzisoft.keepass.database.action.history

import android.content.Context
import com.kunzisoft.keepass.database.element.Database
import com.kunzisoft.keepass.database.element.Entry

class DeleteEntryHistoryDatabaseRunnable (
    context: Context,
    database: Database,
    private val mainEntry: Entry,
    private val entryHistoryPosition: Int,
    saveDatabase: Boolean)
    : com.kunzisoft.keepass.database.action.SaveDatabaseRunnable(context, database, saveDatabase) {

    override fun onStartRun() {
        try {
            database.removeEntryHistory(mainEntry, entryHistoryPosition)
        } catch (e: Exception) {
            setError(e)
        }

        super.onStartRun()
    }
}
