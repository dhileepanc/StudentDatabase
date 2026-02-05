package com.app.studenttask.data.model

data class User(
    val username: String,
    val phone: String, // using String for phone to avoid integer limits/formatting issues
    val password: String
)
