package com.example.repositoriogithub.Retrofit

import com.example.repositoriogithub.Model.ListItens
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IRespositorioService {

    @GET("repositories?q=language:kotlin")
    fun getListRepositorios(@Query("sort") sort : String, @Query("page") page : String ) : Observable<ListItens>
}