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
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
    private lateinit var statusCheckBox: CheckBox
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

        statusCheckBox = view.findViewById(R.id.statusCheck)
        // 체크박스 상태 변경 리스너 설정
        statusCheckBox.setOnCheckedChangeListener { _, isChecked ->
            recyclerViewAdapter.filterProductList(isChecked)
        }

        val button: FloatingActionButton = view.findViewById(R.id.floatingActionButton)
        button.setOnClickListener{
            val fragmentManager = activity?.supportFragmentManager
            val itemDetailsFragment = MakingItemFragment()


            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragmentContainer, itemDetailsFragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        return view;
    }
    // xml파일을 inflate하여 ViewHolder를 생성
    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        private var originalList: ArrayList<product_data> = arrayListOf()
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
                    val seller = snapshot.getString("seller")
                    val detail = snapshot.getString("detail")

//                    var item = snapshot.toObject(product_data::class.java)
                    val item = product_data(title, price, productStatus, detail, seller)
                    productList.add(item!!)
                }
                originalList = productList
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

            init {
                itemView.setOnClickListener{
                    val clickedPosition = bindingAdapterPosition
                    if(clickedPosition != RecyclerView.NO_POSITION){
                        val clickedItem = productList[clickedPosition]

                        val bundle = Bundle().apply {
                            putString("productName", clickedItem.title)
                            putString("productPrice", clickedItem.price)
                            putString("status", clickedItem.productStatus.toString())
                            putString("productDetail", clickedItem.detail)
                            putString("seller", clickedItem.seller)
                        }
                        val fragmentManager = activity?.supportFragmentManager
                        val itemDetailsFragment = ItemDetailsFragment()
                        itemDetailsFragment.arguments = bundle

                        fragmentManager?.beginTransaction()
                            ?.replace(R.id.fragmentContainer, itemDetailsFragment)
                            ?.addToBackStack(null)
                            ?.commit()
                    }
                }
            }
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
        fun filterProductList(showActive: Boolean) {
            productList = if (showActive) {
                ArrayList(originalList.filter { it.productStatus == true })
            } else {
                ArrayList(originalList)
            }
            notifyDataSetChanged()
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

