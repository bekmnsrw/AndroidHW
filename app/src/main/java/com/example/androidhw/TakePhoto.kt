package com.example.androidhw

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContract

class TakePhoto(private var context: Context) : ActivityResultContract<Void?, Bitmap?>() {

    override fun createIntent(context: Context, input: Void?): Intent {
        val galleryIntent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }

        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).takeIf {
            it.resolveActivity(context.packageManager) != null
        }

        val chooserIntent = Intent.createChooser(galleryIntent, "Load photo")

        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

        return chooserIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Bitmap? {
        return if (intent?.extras != null) {
            intent.takeIf { resultCode == Activity.RESULT_OK }?.getParcelableExtra("data")
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, intent.takeIf { resultCode == Activity.RESULT_OK }?.data!!))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, intent.takeIf { resultCode == Activity.RESULT_OK }?.data!!)
            }
        }
    }
}
