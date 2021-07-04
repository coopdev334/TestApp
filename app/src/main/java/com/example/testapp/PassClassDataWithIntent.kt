package com.example.testapp

import java.io.Serializable

// This class is a data class that can be used to pass a data class
// between activies with an intent. Derived from Serializable interface class
// Only contructor used with parameter initializer list

data class PassClassDataWithIntent(
    val fname: String,
    val lname: String,
    val birthday: String,
    val country: String
) : Serializable