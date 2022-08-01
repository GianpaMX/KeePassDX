package com.kunzisoft.keepass.database.element

import android.net.Uri
import androidx.test.platform.app.InstrumentationRegistry
import com.kunzisoft.keepass.database.element.binary.BinaryData
import com.kunzisoft.keepass.model.MainCredential
import com.kunzisoft.keepass.tasks.ProgressTaskUpdater
import com.kunzisoft.keepass.utils.UriUtil
import com.kunzisoft.keepass.R
import org.junit.Test
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class DatabaseTest {
    @Test
    fun empty() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val identifier = context.resources.getIdentifier("db_v4", "raw", context.packageName)
        val inputStream = context.resources.openRawResource(identifier)
        val file = File.createTempFile("tmp", ".kdbx")
        copyStreamToFile(inputStream, file)
        log("tmp file: ${file.absolutePath}")

        val database = Database.getInstance()
        database.clearAndClose(context)
        database.loadData(
            uri = Uri.fromFile(file),
            mainCredential = MainCredential("password"),
            readOnly = true,
            contentResolver = context.contentResolver,
            cacheDirectory = UriUtil.getBinaryDir(context),
            isRAMSufficient = { memoryWanted ->
                BinaryData.canMemoryBeAllocatedInRAM(context, memoryWanted)
            },
            fixDuplicateUUID = false,
            startKeyTimerMessage = R.string.retrieving_db_key,
            startContentTimerMessage = R.string.decrypting_db,
            progressTaskUpdater = object : ProgressTaskUpdater {
                override fun updateMessage(resId: Int) {
                    log(context.getString(resId))
                }
            }
        )

        log("root: ${database.rootGroup?.title}")
        database.rootGroup?.getChildEntries()?.forEach { entry ->
            val originalTitle = entry.title
            log("entry: $originalTitle")

            entry.title = "$originalTitle modified"

            database.updateEntry(entry)
        }
        database.saveData(Uri.fromFile(file), context.contentResolver)
    }
}


private fun log(message: String) {
    println("[TEST TAG] $message")
}

private fun copyStreamToFile(inputStream: InputStream, outputFile: File) {
    inputStream.use { input ->
        val outputStream = FileOutputStream(outputFile)
        outputStream.use { output ->
            val buffer = ByteArray(4 * 1024) // buffer size
            while (true) {
                val byteCount = input.read(buffer)
                if (byteCount < 0) break
                output.write(buffer, 0, byteCount)
            }
            output.flush()
        }
    }
}
