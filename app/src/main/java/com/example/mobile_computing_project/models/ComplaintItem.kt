package com.example.mobile_computing_project.models
import java.sql.Timestamp

data class ComplaintItem(var user: User? = null, var createdAt: Timestamp? = null, var description: String = "", var resolved: Boolean = false)
