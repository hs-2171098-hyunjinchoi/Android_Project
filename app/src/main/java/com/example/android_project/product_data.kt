package com.example.android_project

import com.google.firebase.firestore.DocumentId

data class product_data(
    var title: String? = null,
    var price : String? = null,
    var productStatus: Boolean? = true,
    var detail : String?=null,
    var seller : String?=null,
    var documentId: String?=null
)