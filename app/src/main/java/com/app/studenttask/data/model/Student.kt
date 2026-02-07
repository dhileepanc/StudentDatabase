package com.app.studenttask.data.model

data class Student(
    val id: Int = -1,
    val name: String,
    val className: String,
    val section: String,
    val schoolName: String,
    val gender: String,
    val dob: String,
    val bloodGroup: String,
    val fatherName: String,
    val motherName: String,
    val parentContact: String,
    val address1: String,
    val address2: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val emergencyContact: String,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val photoUri: String = "" // Store URI as string
)
