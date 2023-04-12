package com.tedaindo.customnotification.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tedaindo.customnotification.DashboardActivity
import com.tedaindo.customnotification.R

class FBMessagingServices : FirebaseMessagingService() {

    private val tag = "FCM Service"

    override fun onMessageReceived(p0: RemoteMessage) {
        /*
        p0.notification?.let {
            val clickAction = p0.notification?.clickAction

            Log.d(tag, "From: " + p0.from)
            Log.d(tag, "Notification Message Body: " + it.body)
            Log.d(tag, "Notification Sound: " + it.sound)
            Log.d(tag, "Notification ChannelId: " + it.channelId)

            sendMyNotification(it.title, it.body, it.sound, it.channelId, clickAction)
        }
        */

        val clickAction = p0.data["click_action"]
        val data = p0.data

        Log.d(tag, "From: " + p0.from)
        Log.d(tag, "Notification Message Title: " + data["title"])
        Log.d(tag, "Notification Message Body: " + data["body"])
        Log.d(tag, "Notification Sound: " + data["sound"])
        Log.d(tag, "Notification ChannelId: " + data["channel_id"])

        sendMyNotification(
            data["title"],
            data["body"],
            data["sound"],
            data["channel_id"],
            clickAction
        )
    }

    private fun sendMyNotification(
        title: String?,
        body: String?,
        sound: String?,
        channelId: String?,
        clickAction: String?
    ) {
        val intent = Intent(this, DashboardActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val soundUri: Uri =
            Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.sirinepaspampres)

        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            this,
            "CH_ID"
        )
            .setSmallIcon(R.drawable.ar_solid)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setOngoing(true)
            .setContentIntent(pendingIntent)

        val mNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (soundUri != null) {

                // Changing Default mode of notification
                notificationBuilder.setDefaults(Notification.DEFAULT_SOUND)

                // Creating an Audio Attribute
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()

                // Creating Channel
                val notificationChannel = NotificationChannel(
                    "CH_ID",
                    "Testing_Audio",
                    NotificationManager.IMPORTANCE_HIGH
                )
                notificationChannel.setSound(soundUri, audioAttributes)
                mNotificationManager.createNotificationChannel(notificationChannel)
            }
        }

        mNotificationManager.notify(0, notificationBuilder.build());
    }
}