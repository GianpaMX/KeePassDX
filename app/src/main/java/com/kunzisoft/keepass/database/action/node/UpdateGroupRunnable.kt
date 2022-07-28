package com.kunzisoft.keepass.database.action.node

import android.content.Context
import com.kunzisoft.keepass.database.element.Database
import com.kunzisoft.keepass.database.element.Group
import com.kunzisoft.keepass.database.element.node.Node

class UpdateGroupRunnable constructor(
    context: Context,
    database: Database,
    private val mOldGroup: Group,
    private val mNewGroup: Group,
    save: Boolean,
    afterActionNodesFinish: AfterActionNodesFinish?)
    : ActionNodeDatabaseRunnable(context, database, afterActionNodesFinish, save) {

    override fun nodeAction() {
        if (mOldGroup.nodeId == mNewGroup.nodeId) {
            // WARNING : Re attribute parent and children removed in group activity to save memory
            mNewGroup.addParentFrom(mOldGroup)
            mNewGroup.addChildrenFrom(mOldGroup)

            // Update group with new values
            mNewGroup.touch(modified = true, touchParents = true)

            if (database.rootGroup == mOldGroup) {
                database.rootGroup = mNewGroup
            }
            // Only change data in index
            database.updateGroup(mNewGroup)
        }
    }

    override fun nodeFinish(): ActionNodesValues {
        if (!result.isSuccess) {
            // If we fail to save, back out changes to global structure
            if (database.rootGroup == mNewGroup) {
                database.rootGroup = mOldGroup
            }
            database.updateGroup(mOldGroup)
        }

        val oldNodesReturn = ArrayList<Node>()
        oldNodesReturn.add(mOldGroup)
        val newNodesReturn = ArrayList<Node>()
        newNodesReturn.add(mNewGroup)
        return ActionNodesValues(oldNodesReturn, newNodesReturn)
    }
}
