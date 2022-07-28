package com.kunzisoft.keepass.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import java.io.*
import java.util.*

object DatabaseUriUtil {
    private const val TAG = "UriUtil"

    @Throws(FileNotFoundException::class)
    fun getUriInputStream(contentResolver: ContentResolver, fileUri: Uri?): InputStream? {
        if (fileUri == null)
            return null
        return when {
            isFileScheme(fileUri) -> fileUri.path?.let { FileInputStream(it) }
            isContentScheme2(fileUri) -> contentResolver.openInputStream(fileUri)
            else -> null
        }
    }

    fun isFileScheme(fileUri: Uri): Boolean {
        val scheme = fileUri.scheme
        if (scheme == null || scheme.isEmpty() || scheme.lowercase(Locale.ENGLISH) == "file") {
            return true
        }
        return false
    }

    fun isContentScheme2(fileUri: Uri): Boolean {
        val scheme = fileUri.scheme
        if (scheme != null && scheme.lowercase(Locale.ENGLISH) == "content") {
            return true
        }
        return false
    }

    @Throws(FileNotFoundException::class)
    fun getUriOutputStream(contentResolver: ContentResolver, fileUri: Uri?): OutputStream? {
        if (fileUri == null)
            return null
        return when {
            isFileScheme(fileUri) -> fileUri.path?.let { FileOutputStream(it) }
            isContentScheme2(fileUri) -> {
                try {
                    contentResolver.openOutputStream(fileUri, "wt")
                } catch (e: FileNotFoundException) {
                    Log.e(TAG, "Unable to open stream in `wt` mode, retry in `rwt` mode.", e)
                    // https://issuetracker.google.com/issues/180526528
                    // Try with rwt to fix content provider issue
                    val outStream = contentResolver.openOutputStream(fileUri, "rwt")
                    Log.w(TAG, "`rwt` mode used.")
                    outStream
                }
            }
            else -> null
        }
    }

    fun getBinaryDir(context: Context): File {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            context.applicationContext.noBackupFilesDir
        } else {
            context.applicationContext.filesDir
        }
    }
}
