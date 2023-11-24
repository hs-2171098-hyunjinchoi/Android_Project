package com.example.android_project.navigation

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android_project.R
import com.example.android_project.product_data
import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ListenerRegistration

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeFragment : Fragment(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var recyclerViewAdapter : RecyclerViewAdapter
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("product")
    private var listener : ListenerRegistration? = null
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
            listener = itemsCollectionRef
                .addSnapshotListener{querySnapshot, firebaseFirestoreException ->
//                 ArrayList 비워줌
//                productList.clear()
            if(querySnapshot!=null){ // null 체크 추가

                for (snapshot in querySnapshot!!.documents) {
                    val title = snapshot.getString("title")
                    val price = snapshot.get("price")?.toString()
                    val productStatus: Boolean = snapshot.get("saleStatus") as? Boolean ?: false

//                    var item = snapshot.toObject(product_data::class.java)
                    val item = product_data(title, price, productStatus)
                    productList.add(item!!)
                }
                notifyDataSetChanged()
            }else{
                Log.e("HomeFragment", "querySnapshot null")
            }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
            return ViewHolder(view)
        }
        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var productName : TextView = view.findViewById(R.id.productName)
            var productPrice : TextView = view.findViewById(R.id.productPrice)
            var productStatus : TextView = view.findViewById(R.id.productStatus)
            // statusCheck 사용하실 때 주석 푸시면 됩니다.
//            var statusCheck : CheckBox = view.findViewById(R.id.statusCheck)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = holder as ViewHolder

            viewHolder.productName.text = productList[position].title
            viewHolder.productName.setTypeface(null, Typeface.BOLD)

            viewHolder.productPrice.text = "\u20A9 " + productList[position].price

            if(productList[position].productStatus == true) {
                viewHolder.productStatus.text = "판매 중"
                // 글자색을 초록색으로
                viewHolder.productStatus.setTextColor(Color.GREEN)
            }
            else {
                viewHolder.productStatus.text = "판매 완료"
                // 글자색을 빨간색으로
                viewHolder.productStatus.setTextColor(Color.RED)
            }

            // 인덱스에 따라 배경색 설정
            if (position % 2 == 0) {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#c8c7d7"))
            } else {
                viewHolder.itemView.setBackgroundColor(Color.parseColor("#edecf2"))
            }


        }

        override fun getItemCount(): Int {
            return productList.size
            // 리사이클러뷰의 아이템 총 개수 반환
        }
    }
    // 종료할 때 리스너 해제해야 함 (데이터 변경 감지 종료)
    override fun onDestroyView() {
        super.onDestroyView()
        listener?.remove()
    }
}

