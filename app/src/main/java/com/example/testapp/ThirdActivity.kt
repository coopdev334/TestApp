package com.example.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.testapp.databinding.ActivityThirdBinding

class ThirdActivity : AppCompatActivity() {
    private lateinit var viewThirdBinding: ActivityThirdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewThirdBinding = ActivityThirdBinding.inflate(layoutInflater)
        val viewRoot = viewThirdBinding.root
        setContentView(viewRoot)

        viewThirdBinding.btnThirdAct.setOnClickListener {
            Log.d("ThirdActivity", "viewThirdBinding.setOnClickListener")
            finish()  // Clear ThirdActivity from activity stack and return to SecondActivity
        }
    }
}