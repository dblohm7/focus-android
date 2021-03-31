package org.mozilla.focus.helpers

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore

object DeleteFilesHelper {
    private fun getUriFromDisplayName(context: Context, displayName: String): Uri? {
        val projection = arrayOf(MediaStore.Files.FileColumns._ID)
        val extUri: Uri = MediaStore.Files.getContentUri("external")
        val cursor: Cursor = context.contentResolver.query(
            extUri, projection,
            MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE ?", arrayOf(displayName), null
        )!!
        cursor.moveToFirst()
        return if (cursor.count > 0) {
            val columnIndex: Int = cursor.getColumnIndex(projection[0])
            val fileId: Long = cursor.getLong(columnIndex)
            cursor.close()
            Uri.parse("$extUri/$fileId")
        } else {
            null
        }
    }

    fun deleteFileUsingDisplayName(context: Context, displayName: String): Boolean {
        val uri = getUriFromDisplayName(context, displayName)
        if (uri != null) {
            val resolver = context.contentResolver
            val selectionArgs = arrayOf(displayName)
            try {
                resolver.delete(
                    uri,
                    MediaStore.Files.FileColumns.DISPLAY_NAME + "=?",
                    selectionArgs
                )
                return true
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
        return false
    }
}