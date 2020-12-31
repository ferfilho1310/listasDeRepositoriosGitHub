package com.example.repositoriogithub.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repositoriogithub.Adapter.AdapterRepositorio
import com.example.repositoriogithub.Model.ListItens
import com.example.repositoriogithub.R
import com.example.repositoriogithub.Retrofit.IRespositorioService
import com.example.repositoriogithub.Retrofit.InitRetrofit
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListRepositorios : AppCompatActivity() {

    var mRcMostraRespostiorios: RecyclerView? = null
    var adapterRepositorio: AdapterRepositorio? = null
    var linearLayoutManager: LinearLayoutManager? = null
    var mProgressBarRespositorio : ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mRcMostraRespostiorios = findViewById(R.id.rc_repositorio_git)
        mProgressBarRespositorio = findViewById(R.id.prg_progrressBarListRepositorio)

        adapterRepositorio = AdapterRepositorio()
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mRcMostraRespostiorios!!.adapter = adapterRepositorio
        mRcMostraRespostiorios!!.setHasFixedSize(true)

        mProgressBarRespositorio!!.visibility = View.VISIBLE

        buscaDadosRespositorio()
    }

    fun buscaDadosRespositorio() {

        val iniciaRetrofit = InitRetrofit.init().create(IRespositorioService::class.java)
        val callListRepositorio =
            iniciaRetrofit.getListRepositorios(InitRetrofit.STARS, InitRetrofit.PAGE)

        callListRepositorio.enqueue(object : Callback<ListItens> {
            override fun onResponse(
                call: Call<ListItens>,
                response: Response<ListItens>
            ) {
                    Observable.fromArray(response.body())
                        .subscribe { it ->
                            mProgressBarRespositorio!!.visibility = View.GONE
                            adapterRepositorio!!.adicionarRepositoriosLista(it!!.items!!.sortedByDescending { it.stargazersCount })
                        }
            }

            override fun onFailure(call: Call<ListItens>, t: Throwable) {
                mProgressBarRespositorio!!.visibility = View.GONE
                Log.i("Error Retrofit ", "Error $t")
            }
        })
    }
}