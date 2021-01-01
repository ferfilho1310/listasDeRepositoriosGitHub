package com.example.repositoriogithub.Activity

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repositoriogithub.Adapter.AdapterRepositorio
import com.example.repositoriogithub.Model.Item
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
    var mProgressBarRespositorio: ProgressBar? = null
    var isScrolling = false
    var currentItens: Int = 0
    var totalItens: Int = 0
    var scrollOutIten: Int = 0

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
        adicionarItensRecyclerViewScrollEnd()
    }

    fun buscaDadosRespositorio() {

        val iniciaRetrofit = InitRetrofit.init().create(IRespositorioService::class.java)

        val callListRepositorio =
            iniciaRetrofit.getListRepositorios(InitRetrofit.STARS, InitRetrofit.PAGE.toString())
        callListRepositorio.enqueue(object : Callback<ListItens> {
            override fun onResponse(
                call: Call<ListItens>,
                response: Response<ListItens>
            ) {
                if (response.isSuccessful) {
                    Observable.fromArray(response.body())
                        .subscribe { it ->
                            mProgressBarRespositorio!!.visibility = View.GONE
                            adapterRepositorio!!.adicionarRepositoriosLista(it!!.items!!.sortedByDescending { it.stargazersCount })
                        }
                }
            }

            override fun onFailure(call: Call<ListItens>, t: Throwable) {
                mProgressBarRespositorio!!.visibility = View.GONE
                Toast.makeText(applicationContext, "Erro na busca de dados", Toast.LENGTH_LONG)
                    .show()
            }
        })

    }

    fun adicionarItensRecyclerViewScrollEnd() {
        mRcMostraRespostiorios!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItens = recyclerView.childCount
                totalItens = recyclerView.adapter!!.itemCount
                var linearLayoutManagerSCroll = recyclerView.layoutManager as LinearLayoutManager?
                scrollOutIten = linearLayoutManagerSCroll!!.findLastVisibleItemPosition()

                if ((currentItens + scrollOutIten) >= totalItens) {
                    buscaDadosRespositorio()
                    InitRetrofit.PAGE += 1
                }
            }
        })
    }
}