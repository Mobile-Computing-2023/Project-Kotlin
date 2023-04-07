package com.example.mobile_computing_project.models

data class MenuItem(
    var mid: String = "",
    var name: String = "",
    var price: Int = 0,
    var isVeg: Boolean = false,
    var special: Boolean = false,
    var imgSrc: String = ""
)
