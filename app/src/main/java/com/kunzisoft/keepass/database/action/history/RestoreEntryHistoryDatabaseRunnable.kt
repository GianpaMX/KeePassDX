package com.kunzisoft.keepass.database.action.history

import android.content.Context
import com.kunzisoft.keepass.database.action.node.UpdateEntryRunnable
import com.kunzisoft.keepass.database.element.Database
import com.kunzisoft.keepass.database.element.Entry
import com.kunzisoft.keepass.tasks.ActionRunnable

class RestoreEntryHistoryDatabaseRunnable (
    private val context: Context,
    private val database: Database,
    private val mainEntry: Entry,
    private val entryHistoryPosition: Int,
    private val saveDatabase: Boolean)
    : ActionRunnable() {

    private var updateEntryRunnable: UpdateEntryRunnable? = null

    override fun onStartRun() {
        try {
            val historyToRestore = Entry(mainEntry.getHistory()[entryHistoryPosition])
            // Copy history of main entry in the restore entry
            mainEntry.getHistory().forEach {
                historyToRestore.addEntryToHistory(it)
            }
            // Update the entry with the fresh formatted entry to restore
            updateEntryRunnable = UpdateEntryRunnable(
                context,
                database,
                mainEntry,
                historyToRestore,
                saveDatabase,
                null
            )

            updateEntryRunnable?.onStartRun()

        } catch (e: Exception) {
            setError(e)
        }
    }

    override fun onActionRun() {
        updateEntryRunnable?.onActionRun()
    }

    override fun onFinishRun() {
        updateEntryRunnable?.onFinishRun()
    }
}
