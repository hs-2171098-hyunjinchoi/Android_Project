package com.example.android_project.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.android_project.R
import com.example.android_project.product_data
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class HomeFragment : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("product")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        firestore = FirebaseFirestore.getInstance()

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        recyclerViewAdapter = RecyclerViewAdapter()
        recyclerView.adapter = recyclerViewAdapter

        return view;
    }
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        // xml파일을 inflate하여 ViewHolder를 생성
        var productList : ArrayList<product_data> = arrayListOf()

        init{
            itemsCollectionRef
                .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
//                 ArrayList 비워줌
//                productList.clear()

                for (snapshot in querySnapshot!!.documents) {
                    val name = snapshot.getString("name")
                    val price = snapshot.get("price")?.toString()

//                    var item = snapshot.toObject(product_data::class.java)
                    val item = product_data(name, price)
                    productList.add(item!!)
                }
                notifyDataSetChanged()
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var productName : TextView = view.findViewById(R.id.productName)
            var productPrice : TextView = view.findViewById(R.id.productPrice)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = holder as ViewHolder

            viewHolder.productName.text = productList[position].name
            viewHolder.productPrice.text = productList[position].price

        }

        override fun getItemCount(): Int {
            return productList.size
            // 리사이클러뷰의 아이템 총 개수 반환
        }
    }
}

