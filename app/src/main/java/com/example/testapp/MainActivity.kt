package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*

const val COMPILE_TIME_CONSTANT: String = "Mike TestApp2 Coroutines"

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding  // Use view binding to access views

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater) // Instantiate
        val viewRoot = viewBinding.root
        setContentView(viewRoot)
        Log.d("MainActivity", "onCreate: entered")

        // Get values from textViews and pass data to second activity using serializable data class
        viewBinding.btnOK.setOnClickListener {
            val firstName = viewBinding.etFirstName.text.toString()
            val lastName = viewBinding.etLastName.text.toString()
            val birthDay = viewBinding.etBirthDay.text.toString()
            val country = viewBinding.etCountry.text.toString()
            Log.d("MainActivity", "btnOK.setOnClickListener: $firstName $lastName $birthDay $country")
            Snackbar.make(viewBinding.mainActivity, "User Message", Snackbar.LENGTH_LONG).setAction("Action", View.OnClickListener {
                Log.d("MainActivity", "Snackbar: action selected")
            }).show()

        }

        // Add numbers and display toast to user
        var count = 0
        viewBinding.btnCount.setOnClickListener {
            count++
            viewBinding.tvCount.text = "Count $count"
            Log.d("MainActivity", "btnCount.setOnClickListener: clicked")
            Log.d("MainActivity", "$COMPILE_TIME_CONSTANT")
            Toast.makeText(this, "Toast: User Message", Toast.LENGTH_LONG).show()
        }

        // Start SecondActivity and pass data using putExtra for each item
        viewBinding.btnGoToSecActivity.setOnClickListener {
            Intent(this, SecondActivity::class.java).also {
                val firstName = viewBinding.etFirstName.text.toString()
                val lastName = viewBinding.etLastName.text.toString()
                val birthDay = viewBinding.etBirthDay.text.toString()
                val country = viewBinding.etCountry.text.toString()
                it.putExtra("EXTRA_FNAME", firstName)  // Add values to intent to send to second activity
                it.putExtra("EXTRA_LNAME", lastName)
                it.putExtra("EXTRA_BIRTHDAY", birthDay)
                it.putExtra("EXTRA_COUNTRY", country)
                startActivity(it)
            }

        }

        // Start SecondActivity and pass data using serializable class
        viewBinding.btnDataClass.setOnClickListener {
            val firstName = viewBinding.etFirstName.text.toString()
            val lastName = viewBinding.etLastName.text.toString()
            val birthDay = viewBinding.etBirthDay.text.toString()
            val country = viewBinding.etCountry.text.toString()
            val passingDataClass = PassClassDataWithIntent(firstName, lastName, birthDay, country)
            Intent(this, SecondActivity::class.java).also {
                it.putExtra("EXTRA_DCLASS", passingDataClass)  // Add serializable data class values to intent to send to second activity
                startActivity(it)
            }
        }

        // Coroutine in global scope. This coroutine runs in a new separate
        // thread
        GlobalScope.launch {
            delay(3000L) // coroutine delay only delays this coroutine and not the thread it is running in
            Log.d("MainActivity", "coroutine started in thread name: ${Thread.currentThread().name}")
            delay(3000L) // coroutine delay only delays this coroutine and not the thread it is running in
            Log.d("MainActivity", "coroutine delayed 3 seconds in thread name: ${Thread.currentThread().name}")
            val funcRet = ExampleSuspendFunc() // can only call this function in a coroutine or suspend function
            Log.d("MainActivity", "$funcRet")
        }

        // Coroutine in global scope. This coroutine has a dispatcher to control context thread it runs in.
        // This will also change context of coroutine
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("MainActivity", "coroutine started in IO context thread name: ${Thread.currentThread().name}")
            delay(3000L) // coroutine delay only delays this coroutine and not the thread it is running in

            withContext(Dispatchers.Main){
                Log.d("MainActivity", "coroutine context changed to  thread name: ${Thread.currentThread().name}")
                val funcRet = ExampleSuspendFunc() // can only call this function in a coroutine or suspend function
                Log.d("MainActivity", "$funcRet")
            }
        }

        Log.d("MainActivity", "Main thread name: ${Thread.currentThread().name}")
    }

    override fun onStart() {
        super.onStart()
        Log.d("onstart", "another msg 2")
    }

    // Suspend function. Can only be called in a coroutine or another suspend function
    suspend fun ExampleSuspendFunc(): String {
        delay(3000L)
        return "Retuned from suspend function"
    }


}