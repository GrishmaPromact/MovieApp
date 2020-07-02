package com.grishma.grishmamoviesapp

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.IOException
import java.io.InputStream

 class Utils {

    companion object {
        fun getJsonFromAssets(context: Context, fileName: String?): String? {
            val jsonString: String
            jsonString = try {
                val `is`: InputStream = context.getAssets().open(fileName.toString())
                val size: Int = `is`.available()
                val buffer = ByteArray(size)
                `is`.read(buffer)
                `is`.close()
                String(buffer)
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }
            return jsonString
        }

        fun getImageFromAssetsFile(mContext: Context, fileName: String?): Bitmap? {
            var image: Bitmap? = null
            val am = mContext.resources.assets
            try {
                val `is` = am.open(fileName!!)
                image = BitmapFactory.decodeStream(`is`)
                `is`.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return image
        }
    }
}