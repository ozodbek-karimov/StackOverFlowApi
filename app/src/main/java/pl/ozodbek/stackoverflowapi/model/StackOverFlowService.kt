package pl.ozodbek.stackoverflowapi.model

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object StackOverFlowService {

    private const val BASE_URL = "https://api.stackexchange.com/"

    val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(StackOverFlowApi::class.java)

}