package com.hitzvera.storyapp.widget

import android.content.Intent
import android.widget.RemoteViewsService

/**
 * Created by dicoding on 1/9/2017.
 */

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory =
        StackRemoteViewsFactory(this.applicationContext)
}