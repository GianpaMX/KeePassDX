package com.kunzisoft.keepass.database.element.database

import android.content.res.Resources
import android.os.Parcel
import android.os.Parcelable
import com.kunzisoft.keepass.database.R
import com.kunzisoft.keepass.utils.ObjectNameResource
import com.kunzisoft.keepass.utils.readEnum
import com.kunzisoft.keepass.utils.writeEnum

// Note: We can get away with using int's to store unsigned 32-bit ints
//       since we won't do arithmetic on these values (also unlikely to
//       reach negative ids).
enum class CompressionAlgorithm : ObjectNameResource, Parcelable {

    None,
    GZip;

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeEnum(this)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun getName(resources: Resources): String {
        return when (this) {
            None -> resources.getString(R.string.compression_none)
            GZip -> resources.getString(R.string.compression_gzip)
        }
    }

    companion object CREATOR : Parcelable.Creator<CompressionAlgorithm> {
        override fun createFromParcel(parcel: Parcel): CompressionAlgorithm {
            return parcel.readEnum<CompressionAlgorithm>() ?: None
        }

        override fun newArray(size: Int): Array<CompressionAlgorithm?> {
            return arrayOfNulls(size)
        }
    }

}
