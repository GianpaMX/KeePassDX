package com.kunzisoft.keepass.database.action.node

import android.content.Context
import com.kunzisoft.keepass.database.element.Database
import com.kunzisoft.keepass.database.element.Entry
import com.kunzisoft.keepass.database.element.Group
import com.kunzisoft.keepass.database.element.node.Node

class AddEntryRunnable constructor(
    context: Context,
    database: Database,
    private val mNewEntry: Entry,
    private val mParent: Group,
    save: Boolean,
    afterActionNodesFinish: AfterActionNodesFinish?)
    : ActionNodeDatabaseRunnable(context, database, afterActionNodesFinish, save) {

    override fun nodeAction() {
        mNewEntry.touch(modified = true, touchParents = true)
        mParent.touch(modified = true, touchParents = true)
        database.addEntryTo(mNewEntry, mParent)
    }

    override fun nodeFinish(): ActionNodesValues {
        if (!result.isSuccess) {
            mNewEntry.parent?.let {
                database.removeEntryFrom(mNewEntry, it)
            }
        }

        val oldNodesReturn = ArrayList<Node>()
        val newNodesReturn = ArrayList<Node>()
        newNodesReturn.add(mNewEntry)
        return ActionNodesValues(oldNodesReturn, newNodesReturn)
    }
}
