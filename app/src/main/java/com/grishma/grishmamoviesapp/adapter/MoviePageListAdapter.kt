package com.grishma.grishmamoviesapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.grishma.grishmamoviesapp.R
import com.grishma.grishmamoviesapp.Utils
import com.grishma.grishmamoviesapp.model.Content
import com.grishma.grishmamoviesapp.repository.NetworkState
import kotlinx.android.synthetic.main.list_item_movie.view.*
import kotlinx.android.synthetic.main.network_state_item.view.*

class MoviePageListAdapter(private val context: Context) : PagedListAdapter<Content,RecyclerView.ViewHolder>(MovieDiffCallback()) {

    val MOVIE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState : NetworkState?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       val layoutInflater = LayoutInflater.from(parent.context)
        val view : View

        if(viewType == MOVIE_VIEW_TYPE){
            view = layoutInflater.inflate(R.layout.list_item_movie,parent,false)
            return  MovieItemViewHolder(view)
        } else{
            view = layoutInflater.inflate(R.layout.network_state_item,parent,false)
            return  NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(getItemViewType(position) == MOVIE_VIEW_TYPE){
            (holder as MovieItemViewHolder).bind(getItem(position),context)
        } else{
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    fun hasExtraRow() : Boolean {
        return networkState!=null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(hasExtraRow() && position == itemCount - 1){
            NETWORK_VIEW_TYPE
        } else{
            MOVIE_VIEW_TYPE
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?){
        var previousState = this.networkState
        var hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        var hasExtraRow = hasExtraRow()

        if(hadExtraRow != hasExtraRow){
            if(hadExtraRow){
                notifyItemRemoved(super.getItemCount())
            } else{
                notifyItemInserted(super.getItemCount())
            }
        } else if(hasExtraRow && previousState != newNetworkState){
            notifyItemChanged(itemCount - 1)
        }
    }
    class MovieDiffCallback :DiffUtil.ItemCallback<Content>(){
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
           return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
           return oldItem == newItem
        }

    }

    class MovieItemViewHolder(view: View) : RecyclerView.ViewHolder(view){

        fun bind(movie: Content?,context: Context){
            itemView.movieName.text = movie?.name
            val bitmap = Utils.getImageFromAssetsFile(context,movie?.posterImage)
            Glide.with(itemView.movieName)
                .load(bitmap)
                .centerCrop()
                .placeholder(R.drawable.posterthatismissing)
                .into(itemView.movieImage)
        }
    }

    class NetworkStateItemViewHolder(view: View) :RecyclerView.ViewHolder(view){
        fun bind(networkState: NetworkState?){
            if(networkState != null && networkState == NetworkState.LOADING){
                itemView.progressBar.visibility = View.VISIBLE
            } else{
                itemView.progressBar.visibility = View.GONE
            }

            if(networkState != null && networkState == NetworkState.ERROR){
                itemView.tvErrorMsg.visibility = View.VISIBLE
                itemView.tvErrorMsg.text = networkState.msg
            } else if(networkState != null && networkState == NetworkState.ENDOFLIST){
                itemView.tvErrorMsg.visibility = View.VISIBLE
                itemView.tvErrorMsg.text = networkState.msg
            } else{
                itemView.tvErrorMsg.visibility = View.GONE
            }
        }
    }
}