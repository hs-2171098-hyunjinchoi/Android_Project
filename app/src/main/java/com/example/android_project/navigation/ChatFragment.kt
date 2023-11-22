package com.example.android_project.navigation

import androidx.fragment.app.Fragment

class ChatFragment : Fragment() {


    /*
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    //메세지 보내기 함수(임시)
    private fun sendFCMMessage(serverKey: String,token: String,title:String, messages:String){
        val fcmEndpoint = "https://fcm.googleapis.com/fcm/send"

        val url = URL(fcmEndpoint)
        val connection = url.openConnection() as HttpURLConnection

        connection.doOutput = true
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.setRequestProperty("Authorization", "key=$serverKey")

        val notification = mapOf(
            "title" to title,
            "body" to messages
        )

        val data = mapOf(
            "to" to token,
            "notification" to notification
        )
        val outputStr = data.toString().toByteArray(Charsets.UTF_8)
        val outputStream: OutputStream = connection.outputStream
        outputStream.write(outputStr)
        outputStream.flush()
        val responseCode = connection.responseCode
        Log.d(MyFirebaseMessagingService.TAG,"FCM message sent. Response code: $responseCode")
    }
     */
}