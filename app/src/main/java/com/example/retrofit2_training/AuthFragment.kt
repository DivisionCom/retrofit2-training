package com.example.retrofit2_training

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.retrofit2_training.databinding.FragmentAuthBinding
import com.example.retrofit2_training.retrofit.AuthRequest
import com.example.retrofit2_training.retrofit.MainApi
import com.example.retrofit2_training.retrofit.User
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthFragment : Fragment() {
    private lateinit var binding: FragmentAuthBinding
    private val authViewModel: AuthViewModel by activityViewModels()
    private lateinit var mainApi: MainApi

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRetrofit()

        checkUser()
        login()
    }

    private fun checkUser() {
        binding.btnCheck.setOnClickListener {
            auth(
                AuthRequest(
                    binding.etLogin.text.toString(),
                    binding.etPassword.text.toString()
                )
            )
        }
    }

    private fun auth(authRequest: AuthRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = mainApi.auth(authRequest)
            fillViews(response)
            invalidData(response)
        }
    }

    private fun fillViews(response: Response<User>) {
        val user = response.body()
        requireActivity().runOnUiThread {
            if(user != null) {
                Picasso.get().load(user.image).into(binding.ivUser)
                binding.tvUserName.text = "${user.firstName} ${user.lastName}"
                binding.btnLogin.isEnabled = true
                authViewModel.token.value = user.token
            }
        }
    }

    private fun invalidData(response: Response<User>){
        val message = response.errorBody()?.string()?.let {
            JSONObject(it).getString("message")
        }
        requireActivity().runOnUiThread {
            binding.tvError.text = message
        }
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

    private fun login() {
        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_AuthFragment_to_ProductsFragment)
        }
    }

}