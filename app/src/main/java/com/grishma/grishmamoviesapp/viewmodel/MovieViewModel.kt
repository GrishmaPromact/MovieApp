package com.grishma.grishmamoviesapp.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.grishma.grishmamoviesapp.model.Content
import com.grishma.grishmamoviesapp.repository.MovieDataSourceFactory
import com.grishma.grishmamoviesapp.repository.MoviePageListRepository
import com.grishma.grishmamoviesapp.repository.NetworkState


class MovieViewModel(private val moviePageListRepository: MoviePageListRepository,private val application: Application) : ViewModel() {

    private var factory: MovieDataSourceFactory
    val filterText: MutableLiveData<String> = MutableLiveData()

    val moviePagedList: LiveData<PagedList<Content>> by lazy {
        moviePageListRepository.fetchLiveMoviePagedList(
            application
        )
    }
    init {
        factory = MovieDataSourceFactory(application)
    }

    val networkState: LiveData<NetworkState> by lazy { moviePageListRepository.getNetworkState() }
    fun listIsEmpty(): Boolean {
        return moviePagedList.value?.isEmpty() ?: true
    }
/*
    fun getProjects(query: String, isAlphabetized: Boolean, progress: Int) {
        filterText.value = query
        minBankers.value = progress
        moviePageListRepository.getProjects(factory, query, isAlphabetized, progress)
        moviePagedList.value?.dataSource?.invalidate()
    }*/

}

