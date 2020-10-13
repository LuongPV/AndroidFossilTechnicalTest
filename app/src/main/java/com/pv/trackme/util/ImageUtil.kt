package com.pv.trackme.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream


object ImageUtil {

    fun loadImage(imageUrl: String, imageView: ImageView) {
        Glide.with(imageView).load(imageUrl).into(imageView)
    }

    suspend fun getBitmapFromVectorDrawable(context: Context, drawableId: Int): Bitmap {
        return withContext(Dispatchers.Default) {
            val drawable = ContextCompat.getDrawable(context, drawableId)
            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bitmap
        }
    }

    suspend fun saveBitmapToFile(context: Context, bitmap: Bitmap): String? {
        return withContext(Dispatchers.IO) {
            val externalFilesDir = context.getExternalFilesDir(null)
            externalFilesDir!!.mkdirs()
            val imageOutputPath = externalFilesDir.absolutePath + File.separator + System.currentTimeMillis() + ".jpg"
            try {
                val file = File(imageOutputPath)
                file.createNewFile()
                val outputImageFile = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputImageFile)
                imageOutputPath
            } catch (e: Exception) {
                Timber.w("saveBitmapToFile exception = $e")
                null
            }
        }
    }

}