package com.example.mylowbudgethotel.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mylowbudgethotel.model.Guest

class GuestDatabaseHelper(private val context: Context, private val cursorFactory: SQLiteDatabase.CursorFactory?): SQLiteOpenHelper(context, DATABASE_NAME, cursorFactory, DATABASE_VERSION) {
    override fun onCreate(database: SQLiteDatabase) {
        val createQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_CUSTOMER_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_EMAIL TEXT, $COLUMN_ROOM_NUMBER TEXT)"
        database.execSQL(createQuery)
    }

    override fun onUpgrade(database: SQLiteDatabase, olderVersion: Int, newerVersion: Int) {
        database.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(database)
    }

    fun insertNewGuest(newGuest: Guest){
        val values = ContentValues()
        values.apply {
            put(COLUMN_EMAIL, newGuest.guestEmail)
            put(COLUMN_NAME, newGuest.guestName)
            put(COLUMN_ROOM_NUMBER, newGuest.roomNumber)
        }
        val database = this.writableDatabase
        database.insert(TABLE_NAME, null, values)
        database.close()
    }

    fun getAllGuests(): Cursor? {
        val database = this.readableDatabase

        return database.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    companion object {
        val DATABASE_NAME = "customers.db"
        val DATABASE_VERSION = 1
        val TABLE_NAME = "customers"

        val COLUMN_NAME = "name"
        val COLUMN_EMAIL = "email"
        val COLUMN_CUSTOMER_ID = "customer_id"
        val COLUMN_ROOM_NUMBER = "room"
    }
}