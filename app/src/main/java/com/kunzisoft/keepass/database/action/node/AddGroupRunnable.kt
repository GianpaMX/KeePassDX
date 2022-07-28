package com.kunzisoft.keepass.database.action.node

import android.content.Context
import com.kunzisoft.keepass.database.element.Database
import com.kunzisoft.keepass.database.element.Group
import com.kunzisoft.keepass.database.element.node.Node

class AddGroupRunnable constructor(
    context: Context,
    database: Database,
    private val mNewGroup: Group,
    private val mParent: Group,
    save: Boolean,
    afterActionNodesFinish: AfterActionNodesFinish?)
    : ActionNodeDatabaseRunnable(context, database, afterActionNodesFinish, save) {

    override fun nodeAction() {
        mNewGroup.touch(modified = true, touchParents = true)
        mParent.touch(modified = true, touchParents = true)
        database.addGroupTo(mNewGroup, mParent)
    }

    override fun nodeFinish(): ActionNodesValues {
        if (!result.isSuccess) {
            database.removeGroupFrom(mNewGroup, mParent)
        }

        val oldNodesReturn = ArrayList<Node>()
        val newNodesReturn = ArrayList<Node>()
        newNodesReturn.add(mNewGroup)
        return ActionNodesValues(oldNodesReturn, newNodesReturn)
    }
}
