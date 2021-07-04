package com.example.testapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp.databinding.ActivityMainBinding
import com.example.testapp.databinding.ActivitySecondBinding
import com.google.android.material.snackbar.Snackbar

class SecondActivity : AppCompatActivity() {
    private lateinit var viewSecBinding: ActivitySecondBinding  // Use view binding to access views

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewSecBinding = ActivitySecondBinding.inflate(layoutInflater) // Instantiate class
        val viewRoot = viewSecBinding.root
        setContentView(viewRoot)
        Log.d("MainActivity", "onCreate: This is a msg")

        // Check which intent passing method is used. regular putExtra or serializable data class
        if (!intent.hasExtra("EXTRA_DCLASS")) {
            // Get data from MainActivity intent using putExtra
            val firstName = intent.getStringExtra("EXTRA_FNAME")
            val lastName = intent.getStringExtra("EXTRA_LNAME")
            val birthday = intent.getStringExtra("EXTRA_BIRTHDAY")
            val country = intent.getStringExtra("EXTRA_COUNTRY")
            viewSecBinding.tvSecAct.text = "$firstName,  $lastName, $birthday, $country"
            viewSecBinding.tvDataClass.text = "not used" // not used for this intent method
        } else {
            // Get data from serializable data class to display in textView
            val passedDataClass = intent.getSerializableExtra("EXTRA_DCLASS") as PassClassDataWithIntent
            viewSecBinding.tvDataClass.text = passedDataClass.toString()
            viewSecBinding.tvSecAct.text = "not used" // not used for this intent method
        }

        viewSecBinding.btnGoToMainAct.setOnClickListener {
            Log.d("SecondActivity", "btnGoToMainAct.setOnClickListener")
            finish()  // Clear SecondActivity from activity stack and return to MainActivity
        }

        viewSecBinding.btnGotoThirdAct.setOnClickListener {
            Log.d("SecondActivity", "btnGotoThirdAct.setOnClickListener")
            Intent(this, ThirdActivity::class.java).also {
                startActivity(it)
            }
        }
    }

}