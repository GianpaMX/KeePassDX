package com.kunzisoft.keepass.database.action

import android.content.Context
import android.net.Uri
import com.kunzisoft.keepass.database.element.Database
import com.kunzisoft.keepass.database.exception.DatabaseException
import com.kunzisoft.keepass.tasks.ActionRunnable

open class SaveDatabaseRunnable(protected var context: Context,
                                protected var database: Database,
                                private var saveDatabase: Boolean,
                                private var databaseCopyUri: Uri? = null)
    : ActionRunnable() {

    var mAfterSaveDatabase: ((Result) -> Unit)? = null

    override fun onStartRun() {}

    override fun onActionRun() {
        database.checkVersion()
        if (saveDatabase && result.isSuccess) {
            try {
                database.saveData(databaseCopyUri, context.contentResolver)
            } catch (e: DatabaseException) {
                setError(e)
            }
        }
    }

    override fun onFinishRun() {
        // Need to call super.onFinishRun() in child class
        mAfterSaveDatabase?.invoke(result)
    }
}
