package com.example.repositoriogithub.Activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.repositoriogithub.Adapter.AdapterRepositorio
import com.example.repositoriogithub.R
import com.example.repositoriogithub.Retrofit.IRespositorioService
import com.example.repositoriogithub.Retrofit.InitRetrofit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ListRepositoriosActView : AppCompatActivity() {

    var mRcMostraRespostiorios: RecyclerView? = null
    var adapterRepositorio: AdapterRepositorio? = null
    var mProgressBarRespositorio: ProgressBar? = null

    var currentItens: Int = 0
    var totalItens: Int = 0
    var scrollOutIten: Int = 0

    var mostrarRepositoriosPorQuantidadeEstrelas: Int = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_repositorios_github)

        mRcMostraRespostiorios = findViewById(R.id.rc_repositorio_git)
        mProgressBarRespositorio = findViewById(R.id.prg_ListRepositorio)

        adapterRepositorio = AdapterRepositorio()
        mRcMostraRespostiorios?.layoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        mRcMostraRespostiorios?.adapter = adapterRepositorio
        mRcMostraRespostiorios?.setHasFixedSize(true)

        mProgressBarRespositorio?.visibility = View.VISIBLE

        buscarDadosRespositorio()
        adicionarItensRecyclerViewScrollEnd()
    }

    @SuppressLint("CheckResult")
    fun buscarDadosRespositorio() {

        val iniciaRetrofit = InitRetrofit.init().create(IRespositorioService::class.java)
        val callListRepositorio =
            iniciaRetrofit.getListRepositorios(InitRetrofit.STARS, InitRetrofit.PAGE.toString())

        callListRepositorio.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                mProgressBarRespositorio?.visibility = View.GONE
                adapterRepositorio?.adicionarRepositoriosLista(it?.items!!
                    .sortedByDescending { it.stargazersCount }
                    .filter { it.stargazersCount > mostrarRepositoriosPorQuantidadeEstrelas })
            }, {
                Log.i("Error", "Erro ao buscar os dados $it")
            }, {
                adapterRepositorio?.notifyDataSetChanged()
            })
    }

    fun adicionarItensRecyclerViewScrollEnd() {

        mRcMostraRespostiorios!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val linearLayoutManagerSCroll = recyclerView.layoutManager as LinearLayoutManager?
                currentItens = recyclerView.childCount
                totalItens = recyclerView.adapter!!.itemCount
                scrollOutIten = linearLayoutManagerSCroll!!.findLastVisibleItemPosition()

                if ((currentItens + scrollOutIten) >= totalItens) {
                    buscarDadosRespositorio()
                    InitRetrofit.PAGE += 1
                }
            }
        })
    }
}