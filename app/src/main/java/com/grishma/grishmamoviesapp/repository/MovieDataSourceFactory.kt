package com.grishma.grishmamoviesapp.repository

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.grishma.grishmamoviesapp.model.Content

class MovieDataSourceFactory(var application: Application) : DataSource.Factory<Int, Content>() {

    private var query = ""
    private var isAlphabetized: Boolean = true
    private var minBankers = 0
    val movieLiveDataSource = MutableLiveData<MovieDataSource>()

    override fun create(): DataSource<Int, Content> {
        val movieDataSource = MovieDataSource(application)
        movieLiveDataSource.postValue(movieDataSource)
        return movieDataSource

    }
}


