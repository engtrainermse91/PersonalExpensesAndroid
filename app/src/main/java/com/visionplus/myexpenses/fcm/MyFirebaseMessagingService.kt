package com.visionplus.myexpenses.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.visionplus.myexpenses.R
import com.visionplus.myexpenses.activities.MainActivity
import java.util.*

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        showNotification(p0)
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("FCM_TOKEN",p0)
    }


    private fun showNotification(remoteMessage: RemoteMessage) {
        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "MYEXPENSES_NOTIFICATION_CHANNEL",
                "MYEXPENSES_CHANNEL_NOTIFICATION",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "This is channel for sending my expenses app notification"
            channel.enableLights(true)
            channel.enableVibration(true)
            channel.lightColor = Color.GRAY
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder =
            NotificationCompat.Builder(applicationContext, "MYEXPENSES_NOTIFICATION_CHANNEL")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(remoteMessage.notification!!.title) // title for notification
                .setContentText(remoteMessage.notification!!.body)// message for notification
                .setAutoCancel(true) // clear notification after click

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        val time = Date().time
        val tmpStr = time.toString()
        val last4Str = tmpStr.substring(tmpStr.length - 5)
        val notificationId = Integer.valueOf(last4Str)

        mNotificationManager.notify(notificationId, mBuilder.build())
    }
}