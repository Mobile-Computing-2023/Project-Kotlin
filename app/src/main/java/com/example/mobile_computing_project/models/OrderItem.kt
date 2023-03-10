package com.example.mobile_computing_project.models

import java.sql.Date
import java.sql.Timestamp

data class OrderItem(var user: User? = null, var status: String = "", var createdAt: Long = 0, var amount: Int = 0, var items: List<CartItem>? = null)
