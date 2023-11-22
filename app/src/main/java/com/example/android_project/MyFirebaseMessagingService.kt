package com.example.android_project

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationCompat


class MyFirebaseMessagingService : FirebaseMessagingService() {
    //토큰 변화 시 알림
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        val pref = this.getSharedPreferences("token", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("token", token).apply()
        editor.commit()
    }

    //알림을 받을 때 동작하는 함수
    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: ${message.from}")
        Log.d(TAG, "Message data payload: ${message.data}")
        val msgTitle = message.notification?.title
        val msgBody = message.notification?.body
        Log.d(TAG, "Message Notification title: $msgTitle")
        Log.d(TAG, "Message Notification Body: $msgBody")
        if(msgTitle != null && msgBody != null)
            sendNotification(msgTitle, msgBody);
        //메세지 내용 저장 쿼리
    }

    //알림 창 생성
    private fun sendNotification(title: String, body: String) {
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, uniId, intent,
                PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        val channelId = "firebase-messaging"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
            .setContentTitle(title) // 제목
            .setContentText(body) // 메시지 내용
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, "firebase-messaging channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(uniId, notificationBuilder.build())
    }
    companion object {
        const val TAG = "MyFirebaseMessaging"
    }
}