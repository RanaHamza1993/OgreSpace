package com.brainplow.ogrespace.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.brainplow.ogrespace.R
import com.brainplow.ogrespace.models.StateModel

class SearchKeyWordsAdapter() : RecyclerView.Adapter<SearchKeyWordsAdapter.SearchHolder>() {


    companion object {
        var list: List<String>? = null
        var searchListener: SearchKeyWordsAdapter.ISearchListener? = null
    }

    interface ISearchListener {
        fun onItemClick(position: Int, keyWord: String)
    }

    constructor(keyWordsList: List<String>, searchListener: SearchKeyWordsAdapter.ISearchListener) : this() {
        SearchKeyWordsAdapter.list = keyWordsList
        SearchKeyWordsAdapter.searchListener = searchListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchKeyWordsAdapter.SearchHolder {
        var view: View? = null
        val layoutInflater = LayoutInflater.from(parent.context)

        view = layoutInflater.inflate(R.layout.search_item, parent, false)


        return SearchHolder(view!!)
    }


    override fun getItemCount(): Int {

        val size = SearchKeyWordsAdapter.list?.size
        if (size != null)
            return SearchKeyWordsAdapter.list?.size!!
        else
            return 0
    }

    override fun onBindViewHolder(holder: SearchKeyWordsAdapter.SearchHolder, position: Int) {
        holder.setData(position)
    }

    class SearchHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var searchText: TextView?
        var position: Int? = null

        init {
            searchText = itemView.findViewById(R.id.text)
        }


        fun setData(mPosition: Int) {
            this.position = mPosition
            searchText?.setText(SearchKeyWordsAdapter.list?.get(mPosition))
            itemView.setOnClickListener({
                searchListener?.onItemClick(mPosition, searchText?.text?.toString()!!)
            })
        }

    }

}
