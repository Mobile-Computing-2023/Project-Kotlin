package com.example.mobile_computing_project.models
import java.sql.Timestamp

data class ComplaintItem(var userid: String = "", var createdAt: Long = 0, var description: String = "", var resolved: Boolean = false)
