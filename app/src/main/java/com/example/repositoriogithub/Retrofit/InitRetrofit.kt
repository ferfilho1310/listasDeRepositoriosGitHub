package com.example.repositoriogithub.Retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InitRetrofit {

    companion object{
        var URL = "https://api.github.com/search/"
        var STARS = "stars"
        var PAGE = 0

        fun init(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}