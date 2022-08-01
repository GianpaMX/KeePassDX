/*
 * Copyright 2019 Jeremy Jamet / Kunzisoft.
 *
 * This file is part of KeePassDX.
 *
 *  KeePassDX is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  KeePassDX is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with KeePassDX.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.kunzisoft.keepass.database.exception

import com.kunzisoft.keepass.database.element.node.NodeId
import com.kunzisoft.keepass.database.element.node.Type

abstract class DatabaseException : Exception {
    var parameters: (Array<out String>)? = null

    constructor() : super()
    constructor(message: String) : super(message)
    constructor(message: String, throwable: Throwable) : super(message, throwable)
    constructor(throwable: Throwable) : super(throwable)
}

open class LoadDatabaseException : DatabaseException {
    constructor() : super()
    constructor(string: String) : super(string)
    constructor(throwable: Throwable) : super(throwable)
}

class FileNotFoundDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(string: String) : super(string)
    constructor(exception: Throwable) : super(exception)
}

class InvalidAlgorithmDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class DuplicateUuidDatabaseException : LoadDatabaseException {
    constructor(type: Type, uuid: NodeId<*>) : super() {
        parameters = arrayOf(type.name, uuid.toString())
    }

    constructor(exception: Throwable) : super(exception)
}

class IODatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(string: String) : super(string)
    constructor(exception: Throwable) : super(exception)
}

class KDFMemoryDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class SignatureDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class VersionDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class InvalidCredentialsDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class NoMemoryDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class MoveEntryDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class MoveGroupDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class CopyEntryDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

class CopyGroupDatabaseException : LoadDatabaseException {
    constructor() : super()
    constructor(exception: Throwable) : super(exception)
}

// TODO Output Exception
open class DatabaseOutputException : DatabaseException {
    constructor(string: String) : super(string)
    constructor(string: String, e: Exception) : super(string, e)
    constructor(e: Exception) : super(e)
}
