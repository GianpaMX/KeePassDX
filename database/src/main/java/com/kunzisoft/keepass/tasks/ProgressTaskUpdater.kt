package com.kunzisoft.keepass.tasks

import androidx.annotation.StringRes

interface ProgressTaskUpdater {
    fun updateMessage(@StringRes resId: Int)
}
