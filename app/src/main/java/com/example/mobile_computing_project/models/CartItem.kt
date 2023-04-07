package com.example.mobile_computing_project.models

data class CartItem(
    var cid: String = "",
    var name: String = "",
    var qty: Int = 1,
    var isVeg: Boolean = false,
    var price: Int = 0
)
