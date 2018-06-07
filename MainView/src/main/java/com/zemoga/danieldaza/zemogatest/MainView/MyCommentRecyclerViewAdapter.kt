package com.zemoga.danieldaza.zemogatest.MainView

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView


import com.zemoga.danieldaza.zemogatest.MainView.CommentFragment.OnListFragmentInteractionListener
import com.zemoga.danieldaza.zemogatest.MainView.Models.Comment
import com.zemoga.danieldaza.zemogatest.MainView.Models.Comments

import kotlinx.android.synthetic.main.fragment_comment.view.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyCommentRecyclerViewAdapter(
        private val mValues: Comments,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyCommentRecyclerViewAdapter.ViewHolder>() {

    private var mComments: Comments = mValues
    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Comment
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mComments[position]
        holder.mComment.text = item.body

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mComments.size

    fun updateComments(comments: Comments) {
        mComments = comments
        this.notifyDataSetChanged()
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mComment: TextView = mView.comment

        override fun toString(): String {
            return super.toString() + " '" + mComment.text + "'"
        }
    }
}
