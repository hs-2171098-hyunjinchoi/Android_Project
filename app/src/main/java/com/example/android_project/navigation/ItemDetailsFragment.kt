package com.example.android_project.navigation

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android_project.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class ItemDetailsFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.item_details, container, false)

        // 전달된 데이터 받기
        val productName = arguments?.getString("productName")
        val productPrice = arguments?.getString("productPrice")
        val productDetail = arguments?.getString("productDetail")
        val seller = arguments?.getString("seller")
        val status = arguments?.getString("status")
        val statustext = if(status.toBoolean()){
            "판매중"
        }else{
            "판매 완료"
        }
        val dialogbuilder = AlertDialog.Builder(context)
        val builderItem = inflater.inflate(R.layout.alertdialog, container, false)
        val editTextdialog = builderItem.findViewById<EditText>(R.id.editTextDialog)

        view.findViewById<TextView>(R.id.productName).text = productName
        view.findViewById<TextView>(R.id.productPrice).text = "가격 : $productPrice"
        val statusTextView = view.findViewById<TextView>(R.id.status)
        statusTextView.text = statustext
        statusTextView.setTextColor(if(status.toBoolean())  Color.GREEN else Color.RED)
        val sellerid = view.findViewById<TextView>(R.id.seller)
        sellerid.text = seller
        view.findViewById<TextView>(R.id.productDetail).text = productDetail

        val buttonSeller = view.findViewById<Button>(R.id.buttonSeller)
        if(status.toBoolean()){
            buttonSeller.setOnClickListener {
                Toast.makeText(requireContext(), "오 손이 빠르셨군요!!! 탁월한 선택입니다.", Toast.LENGTH_SHORT).show()
                //버튼 클릭 시 판매자에게 채팅을 보내는 코드 추가해주시면 됩니다.
                if(builderItem.parent != null)
                    ((builderItem.parent) as ViewGroup).removeView(builderItem)
                dialogbuilder.setTitle("메세지 보내기")
                    .setMessage("메세지를 입력하세요")
                    .setView(builderItem)
                    .setPositiveButton("Send"){dialogInterface: DialogInterface, i: Int ->
                        if(editTextdialog.text != null) {
                            sendMessage(sellerid.text.toString(), editTextdialog.text.toString())
                            editTextdialog.text.clear()
                            Toast.makeText(context, "전송이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .setNegativeButton("cancel"){dialogInterface: DialogInterface, i: Int ->

                    }.show()

            }
        }else{
            buttonSeller.setOnClickListener{
                Toast.makeText(requireContext(), "이미 판매된 항목입니다...", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
    private fun sendMessage(receiver: String,message: String) {
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            val chatMessage = Message(Firebase.auth.currentUser?.uid, receiver,message,System.currentTimeMillis())
            Firebase.firestore.collection("chat").add(chatMessage)
        }
    }

}