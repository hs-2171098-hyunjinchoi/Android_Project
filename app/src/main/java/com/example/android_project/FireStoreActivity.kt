package com.example.android_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android_project.navigation.HomeFragment
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FireStoreActivity : AppCompatActivity() {
    private var adapter: HomeFragment.RecyclerViewAdapter ?= null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("product")

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

    }
    private fun updateList(){
//        itemsCollectionRef.get().addOnSuccessListener {
//            val items = mutableListOf<Item>()
//            for(doc in it){
//                items.add(Item(doc))
//            }
//            adapter?.updateList(items)
//
//        }
    }
    private fun addItem(){ //상품을 추가할 때 사용할 코드
//        val name = editItemName.text.toString()
//        if (name.isEmpty()) {
//            Snackbar.make(editItemName, "Input name!", Snackbar.LENGTH_SHORT).show()
//            return
//        }
//        val price = editPrice.text.toString().toInt()
//        val autoID = checkAutoID.isChecked
//        val itemID = editID.text.toString()
//        if (!autoID and itemID.isEmpty()) {
//            Snackbar.make(editID, "Input ID or check Auto-generate ID!", Snackbar.LENGTH_SHORT).show()
//            return
//        }
//        val itemMap = hashMapOf(
//            "name" to name,
//            "price" to price
//        )
//        if (autoID) {
//            itemsCollectionRef.add(itemMap)
//                .addOnSuccessListener { updateList() }.addOnFailureListener {  }
//        } else {
//            itemsCollectionRef.document(itemID).set(itemMap)
//                .addOnSuccessListener { updateList() }.addOnFailureListener {  }
//        }
    }
}