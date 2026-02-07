package com.app.studenttask.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.app.studenttask.data.model.Student
import com.app.studenttask.data.model.User
import javax.inject.Inject

class StudentDatabaseHelper @Inject constructor(
    context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "student_task.db"
        const val DATABASE_VERSION = 2 // Incremented version

        // User Table
        const val TABLE_USER = "users"
        const val COL_USER_NAME = "username"
        const val COL_USER_PHONE = "phone"
        const val COL_USER_PASSWORD = "password"

        // Student Table
        const val TABLE_STUDENT = "students"
        const val COL_STUDENT_ID = "id"
        const val COL_STUDENT_NAME = "name"
        const val COL_STUDENT_CLASS = "class_name"
        const val COL_STUDENT_SECTION = "section"
        const val COL_STUDENT_SCHOOL = "school_name"
        const val COL_STUDENT_GENDER = "gender"
        const val COL_STUDENT_DOB = "dob"
        const val COL_STUDENT_BLOOD = "blood_group"
        const val COL_STUDENT_FATHER = "father_name"
        const val COL_STUDENT_MOTHER = "mother_name"
        const val COL_STUDENT_PARENT_CONTACT = "parent_contact"
        const val COL_STUDENT_ADDR1 = "address1"
        const val COL_STUDENT_ADDR2 = "address2"
        const val COL_STUDENT_CITY = "city"
        const val COL_STUDENT_STATE = "state"
        const val COL_STUDENT_ZIP = "zip_code"
        const val COL_STUDENT_EMERGENCY = "emergency_contact"
        const val COL_STUDENT_LAT = "latitude"
        const val COL_STUDENT_LNG = "longitude"
        const val COL_STUDENT_PHOTO = "photo_uri"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createUserTable = """
            CREATE TABLE $TABLE_USER (
                $COL_USER_NAME TEXT PRIMARY KEY,
                $COL_USER_PHONE TEXT,
                $COL_USER_PASSWORD TEXT
            )
        """.trimIndent()

        val createStudentTable = """
            CREATE TABLE $TABLE_STUDENT (
                $COL_STUDENT_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_STUDENT_NAME TEXT,
                $COL_STUDENT_CLASS TEXT,
                $COL_STUDENT_SECTION TEXT,
                $COL_STUDENT_SCHOOL TEXT,
                $COL_STUDENT_GENDER TEXT,
                $COL_STUDENT_DOB TEXT,
                $COL_STUDENT_BLOOD TEXT,
                $COL_STUDENT_FATHER TEXT,
                $COL_STUDENT_MOTHER TEXT,
                $COL_STUDENT_PARENT_CONTACT TEXT,
                $COL_STUDENT_ADDR1 TEXT,
                $COL_STUDENT_ADDR2 TEXT,
                $COL_STUDENT_CITY TEXT,
                $COL_STUDENT_STATE TEXT,
                $COL_STUDENT_ZIP TEXT,
                $COL_STUDENT_EMERGENCY TEXT,
                $COL_STUDENT_LAT REAL,
                $COL_STUDENT_LNG REAL,
                $COL_STUDENT_PHOTO TEXT
            )
        """.trimIndent()

        db.execSQL(createUserTable)
        db.execSQL(createStudentTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_USER")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENT")
        onCreate(db)
    }

    // --- User Operations ---

    fun registerUser(user: User): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_USER_NAME, user.username)
            put(COL_USER_PHONE, user.phone)
            put(COL_USER_PASSWORD, user.password)
        }
        val result = db.insert(TABLE_USER, null, values)
        return result != -1L
    }

    fun loginUser(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COL_USER_NAME = ? AND $COL_USER_PASSWORD = ?"
        val cursor = db.rawQuery(query, arrayOf(username, password))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    fun isUserExists(username: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_USER WHERE $COL_USER_NAME = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // --- Student Operations ---

    fun addStudent(student: Student): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COL_STUDENT_NAME, student.name)
            put(COL_STUDENT_CLASS, student.className)
            put(COL_STUDENT_SECTION, student.section)
            put(COL_STUDENT_SCHOOL, student.schoolName)
            put(COL_STUDENT_GENDER, student.gender)
            put(COL_STUDENT_DOB, student.dob)
            put(COL_STUDENT_BLOOD, student.bloodGroup)
            put(COL_STUDENT_FATHER, student.fatherName)
            put(COL_STUDENT_MOTHER, student.motherName)
            put(COL_STUDENT_PARENT_CONTACT, student.parentContact)
            put(COL_STUDENT_ADDR1, student.address1)
            put(COL_STUDENT_ADDR2, student.address2)
            put(COL_STUDENT_CITY, student.city)
            put(COL_STUDENT_STATE, student.state)
            put(COL_STUDENT_ZIP, student.zipCode)
            put(COL_STUDENT_EMERGENCY, student.emergencyContact)
            put(COL_STUDENT_LAT, student.latitude)
            put(COL_STUDENT_LNG, student.longitude)
            put(COL_STUDENT_PHOTO, student.photoUri)
        }
        val result = db.insert(TABLE_STUDENT, null, values)
        return result != -1L
    }

    fun getAllStudents(): List<Student> {
        val studentList = ArrayList<Student>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_STUDENT"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STUDENT_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME))

                val className = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_CLASS))
                val schoolName = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_SCHOOL))
                val photoUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_PHOTO))
                

                
                 val student = Student(
                    id = id,
                    name = name,
                    className = className,
                    section = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_SECTION)),
                    schoolName = schoolName,
                    gender = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_GENDER)),
                    dob = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_DOB)),
                    bloodGroup = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_BLOOD)),
                    fatherName = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_FATHER)),
                    motherName = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_MOTHER)),
                    parentContact = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_PARENT_CONTACT)),
                    address1 = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ADDR1)),
                    address2 = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ADDR2)),
                    city = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_CITY)),
                    state = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_STATE)),
                    zipCode = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ZIP)),
                    emergencyContact = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_EMERGENCY)),
                    latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_STUDENT_LAT)),
                    longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_STUDENT_LNG)),
                    photoUri = photoUri
                )
                studentList.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return studentList
    }

    fun getStudentById(id: Int): Student? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENT WHERE $COL_STUDENT_ID = ?", arrayOf(id.toString()))
        var student: Student? = null
        if (cursor.moveToFirst()) {
            student = Student(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_STUDENT_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_NAME)),
                className = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_CLASS)),
                section = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_SECTION)),
                schoolName = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_SCHOOL)),
                gender = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_GENDER)),
                dob = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_DOB)),
                bloodGroup = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_BLOOD)),
                fatherName = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_FATHER)),
                motherName = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_MOTHER)),
                parentContact = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_PARENT_CONTACT)),
                address1 = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ADDR1)),
                address2 = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ADDR2)),
                city = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_CITY)),
                state = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_STATE)),
                zipCode = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_ZIP)),
                emergencyContact = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_EMERGENCY)),
                latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_STUDENT_LAT)),
                longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_STUDENT_LNG)),
                photoUri = cursor.getString(cursor.getColumnIndexOrThrow(COL_STUDENT_PHOTO))
            )
        }
        cursor.close()
        return student
    }

    fun deleteStudent(id: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_STUDENT, "$COL_STUDENT_ID=?", arrayOf(id.toString()))
        return result > 0
    }
}
