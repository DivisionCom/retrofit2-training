package com.example.retrofit2_training

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.retrofit2_training.databinding.ActivityMainBinding
import com.example.retrofit2_training.retrofit.ProductApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
        val productApi = retrofit.create(ProductApi::class.java)

        binding.btnGet.setOnClickListener{
            CoroutineScope(Dispatchers.IO).launch {
                val product = productApi.getProductById()
                runOnUiThread {
                    binding.tvMain.text = product.title
                }
            }
        }
    }

}