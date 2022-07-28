package com.kunzisoft.keepass.database.action.node

import android.content.Context
import com.kunzisoft.keepass.database.element.Database

abstract class ActionNodeDatabaseRunnable(
    context: Context,
    database: Database,
    private val afterActionNodesFinish: AfterActionNodesFinish?,
    save: Boolean)
    : com.kunzisoft.keepass.database.action.SaveDatabaseRunnable(context, database, save) {

    /**
     * Function do to a node action
     */
    abstract fun nodeAction()

    override fun onStartRun() {
        try {
            nodeAction()
        } catch (e: Exception) {
            setError(e)
        }
        super.onStartRun()
    }

    /**
     * Function do get the finish node action
     */
    abstract fun nodeFinish(): ActionNodesValues

    override fun onFinishRun() {
        super.onFinishRun()
        afterActionNodesFinish?.apply {
            onActionNodesFinish(result, nodeFinish())
        }
    }
}
