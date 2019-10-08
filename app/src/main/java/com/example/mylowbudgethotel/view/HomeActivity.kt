package com.example.mylowbudgethotel.view

import android.content.Context
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import com.example.mylowbudgethotel.R
import com.example.mylowbudgethotel.database.GuestDatabaseHelper
import com.example.mylowbudgethotel.model.Guest
import com.example.mylowbudgethotel.util.ErrorLogger
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class HomeActivity : AppCompatActivity() {

    companion object {
        val fileName = "MyCustomerList.txt"
        val filePath = "MyFilePath"
    }

    lateinit var guestDBHelper: GuestDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        guestDBHelper = GuestDatabaseHelper(this, null)
        getAllGuests()
        save_button.setOnClickListener { _ ->
            //saveToInternalStorage()
            //readFromInternalStorage()

//            saveToExternalStorage()
//            readFromExternalStorage()
            addNewGuest()
            getAllGuests()
        }

        read_button.setOnClickListener { _ ->
            //            readFromInternalStorage()
//            readFromExternalStorage()
            getAllGuests()
        }
    }


    private fun saveToInternalStorage() {

        try {
            val fileWriter = openFileOutput(fileName, Context.MODE_APPEND)
            val customerName = customer_name_edittext.text.toString()
            customer_name_edittext.text.clear()
            fileWriter.write(customerName.toByteArray())
            fileWriter.close()
            ErrorLogger.LogError(Throwable("Success in creating file!"))

        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }

        Environment.getExternalStorageState()
    }

    private fun readFromInternalStorage() {
        try {
            val fileInputStream: FileInputStream = openFileInput(fileName)
            val fileInputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(fileInputStreamReader)
            val stringBuilder = StringBuilder()
            var readLine: String? = null

            while ({ readLine = bufferedReader.readLine(); readLine }() != null) {
                stringBuilder.append(readLine)
            }
            info_textview.text = stringBuilder.toString()

        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }
    }

    private fun saveToExternalStorage() {
        try {
            val extFilePath = File(getExternalFilesDir(filePath), fileName)
            val fileOutputStream = FileOutputStream(extFilePath, true)
            val readString = customer_name_edittext.text.toString()
            customer_name_edittext.text.clear()
            fileOutputStream.write(readString.toByteArray())
            fileOutputStream.close()

        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }

    }

    private fun readFromExternalStorage() {
        try {
            val extFilePath = File(getExternalFilesDir(filePath), fileName)
            val fileInputStream = FileInputStream(extFilePath)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder = StringBuilder()
            var readString: String? = null
            while ({ readString = bufferedReader.readLine(); readString }() != null) {
                stringBuilder.append(readString)
            }
            info_textview.text = stringBuilder.toString()
        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }
    }


    private fun addNewGuest() {

        val customerName = customer_name_edittext.text.toString().toUpperCase()
        val customerEmail = customer_email_edittext.text.toString().toUpperCase()
        val customerRoom = customer_roomnumber_edittext.text.toString().toUpperCase()

        customer_name_edittext.text.clear()
        customer_email_edittext.text.clear()
        customer_roomnumber_edittext.text.clear()
        try {
            guestDBHelper.insertNewGuest(Guest(customerName, customerEmail, customerRoom))
        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }
    }

    private fun getAllGuests() {
        val dbCursor = guestDBHelper.getAllGuests()
        dbCursor?.moveToFirst()
        val stringBuilder = StringBuilder()
        try {
            dbCursor?.let { myCursor ->

                while (myCursor.moveToNext()) {
                    stringBuilder.append(
                        "${myCursor.getInt(myCursor.getColumnIndex(GuestDatabaseHelper.COLUMN_CUSTOMER_ID))} | ${myCursor.getString(
                            myCursor.getColumnIndex(GuestDatabaseHelper.COLUMN_NAME)
                        )} ${dbCursor.getString(myCursor.getColumnIndex(GuestDatabaseHelper.COLUMN_EMAIL))} ${myCursor.getString(
                            myCursor.getColumnIndex(GuestDatabaseHelper.COLUMN_ROOM_NUMBER)
                        )}\n"
                    )
                }
                myCursor.close()
            }

            info_textview.text = stringBuilder.toString()
        } catch (throwable: Throwable) {
            ErrorLogger.LogError(throwable)
        }

    }
}
