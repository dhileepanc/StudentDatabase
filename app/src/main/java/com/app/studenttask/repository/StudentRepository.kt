package com.app.studenttask.repository

import com.app.studenttask.data.local.StudentDatabaseHelper
import com.app.studenttask.data.model.Student
import com.app.studenttask.data.model.User
import javax.inject.Inject

class StudentRepository @Inject constructor(
    private val dbHelper: StudentDatabaseHelper
) {

    // User Operations
    fun registerUser(user: User): Boolean = dbHelper.registerUser(user)
    fun loginUser(username: String, password: String): Boolean = dbHelper.loginUser(username, password)
    fun isUserExists(username: String): Boolean = dbHelper.isUserExists(username)

    // Student Operations
    fun addStudent(student: Student): Boolean = dbHelper.addStudent(student)
    fun getStudentById(id: Int): Student? = dbHelper.getStudentById(id)
    fun getAllStudents(): List<Student> = dbHelper.getAllStudents()
    fun deleteStudent(id: Int): Boolean = dbHelper.deleteStudent(id)
}
