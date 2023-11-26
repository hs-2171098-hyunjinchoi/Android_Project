package com.example.android_project.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.android_project.R
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MakingItemFragment : Fragment(){
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("product")
    private var userID : String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.making_item, container, false)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val titleEditText: EditText = view.findViewById(R.id.productName)
        val priceEditText: EditText = view.findViewById(R.id.productPrice)
        val sellerEditText: TextView = view.findViewById(R.id.seller)
        val detailEditText: EditText = view.findViewById(R.id.productDetail)

        if (currentUser != null) {
            userID = currentUser.email
            sellerEditText.setText(userID)
        }else{
            sellerEditText.setText("?")
        }

        val saveButton: Button = view.findViewById(R.id.createButton)
        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val price = priceEditText.text.toString()
            val seller = userID
            val detail = detailEditText.text.toString()

            if (title.isEmpty() || price.isEmpty() || detail.isEmpty()) {
                Toast.makeText(requireContext(), "모든 항목을 채워주세요.", Toast.LENGTH_SHORT).show()
            }else{
                val newItem = hashMapOf(
                    "title" to title,
                    "price" to price,
                    "saleStatus" to true,
                    "seller" to seller,
                    "detail" to detail
                )

                itemsCollectionRef.add(newItem)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "성공적으로 저장되었습니다!", Toast.LENGTH_SHORT).show()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.replace(R.id.fragmentContainer, HomeFragment())
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                    .addOnFailureListener { e ->
                        Log.e("MakingItemFragment", "Error adding document", e)
                    }
            }
        }
        return view
    }
}