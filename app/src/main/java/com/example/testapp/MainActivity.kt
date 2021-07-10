package com.example.testapp

import android.content.Intent
import android.content.IntentFilter
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
    private lateinit var broadcastRec : AirplaneBroadCastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater) // Instantiate
        val viewRoot = viewBinding.root
        setContentView(viewRoot)
        Log.d("MainActivityTestApp", "onCreate: entered")
        Log.d("MainActivityTestApp", "Main thread Started. name: ${Thread.currentThread().name}")

        // Get values from textViews and pass data to second activity using serializable data class
        viewBinding.btnOK.setOnClickListener {
            val firstName = viewBinding.etFirstName.text.toString()
            val lastName = viewBinding.etLastName.text.toString()
            val birthDay = viewBinding.etBirthDay.text.toString()
            val country = viewBinding.etCountry.text.toString()
            Log.d("MainActivityTestApp", "btnOK.setOnClickListener: $firstName $lastName $birthDay $country")
            Snackbar.make(viewBinding.mainActivity, "User Message", Snackbar.LENGTH_LONG).setAction("Action", View.OnClickListener {
                Log.d("MainActivityTestApp", "Snackbar: action selected")
            }).show()

        }

        // Add numbers and display toast to user
        var count = 0
        viewBinding.btnCount.setOnClickListener {
            count++
            viewBinding.tvCount.text = "Count $count"
            Log.d("MainActivityTestApp", "btnCount.setOnClickListener: clicked")
            Log.d("MainActivityTestApp", "$COMPILE_TIME_CONSTANT")
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
        // thread. Coroutines do not block the thread they run in.
        GlobalScope.launch {
            delay(3000L) // coroutine delay only delays this coroutine and not the thread it is running in
            Log.d("MainActivityTestApp", "coroutine 1 started in thread name: ${Thread.currentThread().name}")
            delay(3000L) // coroutine delay only delays this coroutine and not the thread it is running in
            Log.d("MainActivityTestApp", "coroutine delayed 3 seconds in thread name: ${Thread.currentThread().name}")
            val funcRet = ExampleSuspendFunc() // can only call this function in a coroutine or suspend function
            Log.d("MainActivityTestApp", "$funcRet")
        }

        // Coroutine in global scope. This coroutine has a dispatcher to control context (thread) it runs in.
        // withContext() is used to change context of the current coroutine. This can be used to do
        // something that should not be done in main thread so UI is not blocked. Start thread in NON main context then
        // after that code is done, switch to main thread to do some UI or something.
        // Note: The UI can only be changed in the main thread. So any updates or changes to views can only be done in main.
        // You can start a new thread with GlobalScope.launch(newSingleThreadContext("MyNewThreadName")) and coroutine will
        // run in that new thread.
        GlobalScope.launch(Dispatchers.IO) {
            Log.d("MainActivityTestApp", "coroutine 2 started in IO context thread name: ${Thread.currentThread().name}")
            delay(3000L) // coroutine delay only delays this coroutine and not the thread it is running in

            withContext(Dispatchers.Main){
                Log.d("MainActivityTestApp", "coroutine 2 context changed to  thread name: ${Thread.currentThread().name}")
                val funcRet = ExampleSuspendFunc() // can only call this function in a coroutine or suspend function
                Log.d("MainActivityTestApp", "$funcRet")
            }
        }

        // Coroutines return a job id which can be used to control the coroutine
        val gsCoroutineJob = GlobalScope.launch(Dispatchers.Default) {
            // withTimeout if code within braces takes longer that 3 secs cancel coroutine job
            withTimeout(3000L){
                repeat(5){
                    Log.d("MainActivityTestApp", "repeat count: ${it.toString()}")
                    for (i in 1..30){
                        Log.d("MainActivityTestApp", "For loop count: $i")

                        // Check if coroutine cancelled with a call to gsCoroutineJob.cancel()
                        if (isActive){
                            Log.d("MainActivityTestApp", "still active")
                        }
                    }
                }
            }

        }

        // runBlocking will actually block the thread its called in. In this case the main thread
        // will be blocked here.
        runBlocking {
            delay(3000L) // this will block main thread for 3 secs
            ExampleSuspendFunc() // can call suspend functions here which will block main thread

            // Can also launch a coroutine here but GlobalScope not used since this is already in a coroutine scope
            launch {
                // now running in new coroutine. code after this launch block will continue to run, not blocked.
            }

            runBlocking {
                // This is joined to above coroutine with gsCoroutineJob variable. This will block
                // main thread until above coroutine is finished.
                gsCoroutineJob.join() // waits here for coroutine to complete
                // gsCoroutineJob.cancel()   // This can cancel the coroutine
            }
        }

        // Coroutine using async() call for coroutines that return a result
        GlobalScope.launch(Dispatchers.Default) {
            val asyncCall1 = async {
                delay(3000L)
                return@async "done1"
            }
            val asyncCall2 = async {
                delay(3000L)
                return@async "done2"
            }
            Log.d("MainActivityTestApp", "${asyncCall1.await()}") // wait here for async call to finish
            Log.d("MainActivityTestApp", "${asyncCall2.await()}") // wait here for async call to finish
            asyncCall2.await() // wait here for async call to finish

        }

        // Create AirplaneBroadcastReceiver class object in var
        broadcastRec = AirplaneBroadCastReceiver()

        // Create IntentFilter to register what intent is to be received by this app
        IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED).also {
            registerReceiver(broadcastRec, it)
        }
        Log.d("MainActivityTestApp", "onCreate: exited")
    }

    override fun onStart() {
        super.onStart()
        Log.d("onstart", "another msg 2")
    }

    // Suspend function. Can only be called in a coroutine or another suspend function
    suspend fun ExampleSuspendFunc(): String {
        delay(3000L) // simulate some lengthy function call such as network call.
        return "Retuned from suspend function"
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(broadcastRec) // unregister to avoid memory leaks if MainActivity context is stopped
    }


}