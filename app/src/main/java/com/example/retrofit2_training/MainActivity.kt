package com.example.retrofit2_training

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit2_training.adapter.ProductAdapter
import com.example.retrofit2_training.databinding.ActivityMainBinding
import com.example.retrofit2_training.retrofit.AuthRequest
import com.example.retrofit2_training.retrofit.MainApi
import com.example.retrofit2_training.retrofit.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://dummyjson.com/"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val mainApi = retrofit.create(MainApi::class.java)

        adapter = ProductAdapter()
        binding.rcView.layoutManager = LinearLayoutManager(this)
        binding.rcView.adapter = adapter

        var user: User? = null

        CoroutineScope(Dispatchers.IO).launch {
            user = mainApi.auth(AuthRequest(
                username = "atuny0",
                password = "9uQFF1Lh"
            ))
            runOnUiThread {
                binding.svProducts.queryHint = "Enter your request, ${user?.username}"
            }
        }

        binding.svProducts.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(text: String?): Boolean {
                CoroutineScope(Dispatchers.IO).launch {
                    val productObject = text?.let { mainApi.getSearchProductsAuth(
                        user?.token ?: "", it) }

                    runOnUiThread {
                        binding.apply {
                            adapter.submitList(productObject?.products)
                        }
                    }
                }
                return true
            }

        })

        CoroutineScope(Dispatchers.IO).launch {
            val productObject = mainApi.getAllProducts()

            runOnUiThread{
                binding.apply {
                    adapter.submitList(productObject.products)
                }
            }
        }

    }

}