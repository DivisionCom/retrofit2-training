package com.example.retrofit2_training

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AuthViewModel: ViewModel() {
    val token = MutableLiveData<String>()
}