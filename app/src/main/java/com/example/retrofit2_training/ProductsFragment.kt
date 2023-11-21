package com.example.retrofit2_training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofit2_training.adapter.ProductAdapter
import com.example.retrofit2_training.databinding.FragmentProductsBinding
import com.example.retrofit2_training.retrofit.MainApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var mainApi: MainApi
    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()
        initRvProducts()

        authViewModel.token.observe(viewLifecycleOwner){ token ->
            CoroutineScope(Dispatchers.IO).launch {
                val list = mainApi.getAllProducts()
            }
        }

    }

    private fun initRvProducts() = with(binding){
        adapter = ProductAdapter()
        rvProducts.layoutManager = LinearLayoutManager(context)
        rvProducts.adapter = adapter
    }

    private fun initRetrofit() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build()
        mainApi = retrofit.create(MainApi::class.java)
    }

}