package com.grishma.grishmamoviesapp.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.google.gson.Gson
import com.grishma.grishmamoviesapp.Utils
import com.grishma.grishmamoviesapp.model.Content
import com.grishma.grishmamoviesapp.model.Movie

class MovieDataSource(var application: Application) : PageKeyedDataSource<Int, Content>() {
    private var page = 1
    private var totalPages = 3
    val networkState : MutableLiveData<NetworkState> = MutableLiveData()

    val response: MutableLiveData<MutableList<Content>> by lazy { MutableLiveData<MutableList<Content>>() }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, Content>) {
        Log.d("HI:", "API call requested on ->" + Thread.currentThread().name)
        networkState.postValue(NetworkState.LOADING)
        try {
            val movieString = Utils.getJsonFromAssets(application, "PAGE-PAGE$page.json")
            val gson = Gson()
            val movie: Movie = gson.fromJson(movieString, Movie::class.java)
            callback.onResult(movie.page.contentItems.content,null,page+1)
            networkState.postValue(NetworkState.LOADED)
            response.postValue((movie.page.contentItems.content))

        } catch (exception: Exception) {
            Log.d("HI:", "API call requested on ->" + exception.printStackTrace())
            networkState.postValue(NetworkState.ERROR)
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Content>) {
        networkState.postValue(NetworkState.LOADING)
        Log.d("HI:", "API call requested on ->" + Thread.currentThread().name)
        try {
            val movieString = Utils.getJsonFromAssets(application, "PAGE-PAGE${params.key}.json")
            val gson = Gson()
            val movie: Movie = gson.fromJson(movieString, Movie::class.java)
            if(totalPages >= params.key) {
                callback.onResult(movie.page.contentItems.content,  params.key + 1)

                networkState.postValue(NetworkState.LOADED)
            } else{
                networkState.postValue(NetworkState.ENDOFLIST)
            }
            response.postValue((movie.page.contentItems.content))

        } catch (exception: Exception) {
            Log.d("HI:", "API call requested on ->" + exception.printStackTrace())
            networkState.postValue(NetworkState.ERROR)
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Content>) {

    }

}