package com.example.mobile_computing_project.models
data class ComplaintItem(var user: User? = null, var createdAt: Long = 0, var description: String = "", var resolved: Boolean = false)
