package com.nguyen.topcodertaas_kt

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import java.io.File
import java.io.FileOutputStream

class Utils {
    companion object {
        val WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST = 101

        // take a screen shot of the current fragment and share it
        fun shareScreenShot(fragment: Fragment) {
            getPermission(fragment)
            val bitmap = takeScreenshot(fragment.requireActivity())
            val file = saveBitmap(fragment.context, bitmap)
            shareImage(fragment, file)
        }

        // request permission to write data to external storage
        fun onRequestPermissionsResult(
            fragment: Fragment,
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            if (grantResults.count() == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    fragment.context,
                    "Write External Storage permission granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                } else {
                    Toast.makeText(
                        fragment.context,
                        "Write External Storage permission denied",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // what to do when permission is granted
        fun getPermission(fragment: Fragment) {
            if (ContextCompat.checkSelfPermission(
                    fragment.requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                }
                fragment.requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_EXTERNAL_STORAGE_PERMISSIONS_REQUEST
                )
            }
        }

        // take a screen shot, except in screen with google map
        fun takeScreenshot(activity: Activity): Bitmap {
            val rootView = activity.window.decorView.rootView
            rootView.setDrawingCacheEnabled(true)
            val bitmap = Bitmap.createBitmap(rootView.drawingCache)
            rootView.setDrawingCacheEnabled(false)
            return bitmap
        }

        // save a bitmap into a file
        fun saveBitmap(context: Context?, bitmap: Bitmap): File {
            // val imagePath = File(Environment.getExternalStorageDirectory().absolutePath + "/screenshot.png")
            val imagePath = File(context?.getExternalFilesDir(null)?.absolutePath + "/screenshot.png")
            var fos: FileOutputStream
            try {
                fos = FileOutputStream(imagePath)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.flush()
                fos.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return imagePath
        }

        // share an image (based on its file path) with the apps available
        fun shareImage(fragment: Fragment, imagePath: File) {
            val uri = FileProvider.getUriForFile(fragment.requireContext(),
                fragment.context?.applicationContext?.packageName + ".provider",
                imagePath)
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "image/*"
            // sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.title_total_stats))
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
            try {
                fragment.startActivity(
                    Intent.createChooser(
                        sharingIntent,
                        fragment.getString(R.string.title_total_stats)
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        fun toString(number: Int): String {
            return String.format("%,d", number)
        }
    }
}