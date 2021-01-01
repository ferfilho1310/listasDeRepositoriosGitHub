package com.example.repositoriogithub

import com.example.repositoriogithub.Adapter.AdapterRepositorio
import com.example.repositoriogithub.Retrofit.IRespositorioService
import com.example.repositoriogithub.Retrofit.InitRetrofit
import io.reactivex.rxjava3.core.Observable
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@RunWith(JUnit4::class)
class RequestRetrofitApiTest {

    @Test
    fun returnRequestRetrofitResponse200() {

        val stars = "stars"
        val page = "page"
        val URL = "https://api.github.com/search/"

        val iniciaRetrofit = InitRetrofitTest.init(URL)
        val callListRepositorio =
            iniciaRetrofit.getListRepositorios(stars, page).execute()

        Assert.assertEquals("200", callListRepositorio.code().toString())
    }

    @Test
    fun returnRequestRetrofitResponseBody_notNull() {

        val stars = "stars"
        val page = "page"
        val URL = "https://api.github.com/search/"

        val iniciaRetrofit = InitRetrofitTest.init(URL)
        val callListRepositorio =
            iniciaRetrofit.getListRepositorios(stars, page).execute()

        val bodyNotNull = callListRepositorio.body()

        Assert.assertNotNull(bodyNotNull)
    }

    @Test
    fun returnRequestRetrofitResponse_BodyNullBecauseUrlWrong(){

        val stars = "stars"
        val page = "page"
        val URL = "https://api.github.com/searc/"

        val iniciaRetrofit = InitRetrofitTest.init(URL)
        val callListRepositorio =
            iniciaRetrofit.getListRepositorios(stars, page).execute()

        val bodyNull = callListRepositorio.body()

        Assert.assertNotNull(bodyNull)
    }
    
    class InitRetrofitTest {
        companion object{
            fun init(url : String): IRespositorioService {
                val retrofit = Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return retrofit.create(IRespositorioService::class.java)
            }
        }
    }
}