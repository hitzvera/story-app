package com.hitzvera.storyapp.widget


import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.hitzvera.storyapp.ImagesBannerWidget
import com.hitzvera.storyapp.R
import java.util.*

/**
 * Created by dicoding on 1/9/2017.
 */

internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        //Ini berfungsi untuk melakukan refresh saat terjadi perubahan.
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.photo_profile))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_capture))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources,
            R.drawable.ic_place_holder
        ))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_gallery))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.ic_password_24))
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        val extras = bundleOf(
            ImagesBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}