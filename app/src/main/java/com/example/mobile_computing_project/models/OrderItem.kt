package com.example.mobile_computing_project.models

data class OrderItem(var oid: String = "",var user: User? = null, var status: String = "Pending", var createdAt: Long = 0, var amount: Int = 0, var items: List<CartItem>? = null)
