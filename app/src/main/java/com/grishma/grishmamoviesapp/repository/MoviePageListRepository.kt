package com.grishma.grishmamoviesapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.grishma.grishmamoviesapp.model.Content

class MoviePageListRepository(application: Application) {

    lateinit var moviePagedList: LiveData<PagedList<Content>>
    lateinit var movieDataSourceFactory: MovieDataSourceFactory

    val config = PagedList.Config.Builder()
        .setEnablePlaceholders(true)
        .setPageSize(20)
        .build()

/*
    fun getProjects(movieSourceFactory: MovieSourceFactory, query: String, isAlphabetized: Boolean,minBankers:Int){
        movieSourceFactory.search(query, isAlphabetized)
    }*/

    fun fetchLiveMoviePagedList(application: Application) : LiveData<PagedList<Content>>{
        movieDataSourceFactory = MovieDataSourceFactory(application)


        moviePagedList = LivePagedListBuilder(movieDataSourceFactory,config).build()
        return moviePagedList
    }
    fun getNetworkState() : LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource,NetworkState>(movieDataSourceFactory.movieLiveDataSource,
        MovieDataSource::networkState)
    }

    /*fun performSearch(query: String) {
        val newData = movieDatabaseDao.searchProjectByName(query)
        *//* Won't work . LiveData already taken, unless you change the value*//*
        moviePagedList = LivePagedListBuilder(newData, config).build()
    }*/
}