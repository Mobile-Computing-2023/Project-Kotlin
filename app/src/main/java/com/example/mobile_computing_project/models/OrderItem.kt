package com.example.mobile_computing_project.models

import java.sql.Timestamp

data class OrderItem(var user: User? = null, var status: String = "", var createdAt: Timestamp? = null, var amount: Int = 0, var items: List<MenuItem>? = null)
