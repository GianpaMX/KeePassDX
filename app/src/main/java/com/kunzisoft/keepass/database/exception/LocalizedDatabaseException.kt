package com.kunzisoft.keepass.database.exception

import android.content.res.Resources
import com.kunzisoft.keepass.R

fun DatabaseException.getLocalizedMessage(resources: Resources): String = parameters?.let {
    when (this) {
        is DuplicateUuidDatabaseException -> resources.getString(R.string.invalid_db_same_uuid, *it)
        else -> null
    }
} ?: when (this) {
    is LoadDatabaseException -> resources.getString(R.string.error_load_database)
    is FileNotFoundDatabaseException -> resources.getString(R.string.file_not_found_content)
    is InvalidAlgorithmDatabaseException -> resources.getString(R.string.invalid_algorithm)
    is IODatabaseException -> resources.getString(R.string.error_load_database)
    is KDFMemoryDatabaseException -> resources.getString(R.string.error_load_database_KDF_memory)
    is SignatureDatabaseException -> resources.getString(R.string.invalid_db_sig)
    is VersionDatabaseException -> resources.getString(R.string.unsupported_db_version)
    is InvalidCredentialsDatabaseException -> resources.getString(R.string.invalid_credentials)
    is NoMemoryDatabaseException -> resources.getString(R.string.error_out_of_memory)
    is MoveEntryDatabaseException -> resources.getString(R.string.error_move_entry_here)
    is MoveGroupDatabaseException -> resources.getString(R.string.error_move_group_here)
    is CopyEntryDatabaseException -> resources.getString(R.string.error_copy_entry_here)
    is CopyGroupDatabaseException -> resources.getString(R.string.error_copy_group_here)
    is DatabaseOutputException -> resources.getString(R.string.error_save_database)
    else -> resources.getString(R.string.error_load_database)
}
