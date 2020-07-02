package com.grishma.grishmamoviesapp.view

import android.content.res.Configuration
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.grishma.grishmamoviesapp.R
import com.grishma.grishmamoviesapp.adapter.MoviePageListAdapter
import com.grishma.grishmamoviesapp.repository.MoviePageListRepository
import com.grishma.grishmamoviesapp.repository.NetworkState
import com.grishma.grishmamoviesapp.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MovieViewModel

    lateinit var moviePageListRepository: MoviePageListRepository
    val movieAdapter = MoviePageListAdapter( this)

    private var lastFilter = ""
    private var lastProgress = 0
    private var isAlphabetized = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        moviePageListRepository = MoviePageListRepository(application)
        viewModel = getViewModel()

        tvTitle.text = "Romantic Comedy"

        imgSearch.setOnClickListener {
            imgBackBtn.visibility = View.GONE
            tvTitle.visibility = View.GONE
            imgSearch.visibility = View.GONE
            etSearch.visibility = View.VISIBLE
            imgCloseBtn.visibility = View.VISIBLE
        }

        imgCloseBtn.setOnClickListener {
            imgBackBtn.visibility = View.VISIBLE
            tvTitle.visibility = View.VISIBLE
            imgSearch.visibility = View.VISIBLE
            etSearch.text.clear()
            etSearch.visibility = View.GONE
            imgCloseBtn.visibility = View.GONE
        }

        imgBackBtn.setOnClickListener {
            finish()
        }
        var layoutManager : GridLayoutManager
        if (resources.configuration.orientation === Configuration.ORIENTATION_PORTRAIT) {
             layoutManager = GridLayoutManager(this, 3)
        } else {
             layoutManager = GridLayoutManager(this, 5)
        }

        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if(viewType == movieAdapter.MOVIE_VIEW_TYPE) return 1
                else return 3
            }
        }
        rvMovies.apply {
            rvMovies.layoutManager = layoutManager
            rvMovies.adapter = movieAdapter
        }

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })


        viewModel.networkState.observe(this, Observer {
            mainProgress.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            tvTxtProgressError.visibility = if(viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }
        })

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                //viewModel.getProjects(s.toString(), isAlphabetized, lastProgress)
            }
        })

    }

    fun getViewModel() : MovieViewModel{
        return ViewModelProvider(this,object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return  MovieViewModel(moviePageListRepository,application) as T
            }

        })[MovieViewModel::class.java]
    }


}


