package com.zemoga.danieldaza.zemogatest.MainView

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.daimajia.swipe.SwipeLayout
import com.zemoga.danieldaza.zemogatest.MainView.Models.Post
import com.zemoga.danieldaza.zemogatest.MainView.Models.Posts


import com.zemoga.danieldaza.zemogatest.MainView.PostFragment.OnListFragmentInteractionListener

import kotlinx.android.synthetic.main.fragment_post.view.*

/**
 * [RecyclerView.Adapter] that can display a [Post] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class MyPostRecyclerViewAdapter(
        private val mValues: Posts,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<MyPostRecyclerViewAdapter.ViewHolder>(), SwipeLayout.SwipeListener {

    override fun onOpen(layout: SwipeLayout?) {
    }

    override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
    }

    override fun onStartOpen(layout: SwipeLayout?) {
    }

    override fun onStartClose(layout: SwipeLayout?) {
    }

    override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
    }

    override fun onClose(layout: SwipeLayout?) {
    }

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Post
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            if (mListener !== null && mListener.onListFragmentInteraction(item)) {
                this.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_post, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mTitle.text = item.title
        holder.mNotificationIcon.visibility = if (item.isRead()) GONE else VISIBLE
        holder.mFavIcon.visibility = if (item.isFavorite()) VISIBLE else GONE

        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    fun removeAll() {
        while (mValues.size != 0) {
            mValues.removeAt(0)
            notifyItemRemoved(0)
        }
    }

    fun removeAt(position: Int) {
        mValues.removeAt(position)
        notifyItemRemoved(position)
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mNotificationIcon: ImageView = mView.notification_icon
        val mTitle: TextView = mView.title
        val mFavIcon: ImageView = mView.fav_icon

        override fun toString(): String {
            return super.toString() + " '" + mTitle.text + "'"
        }
    }
}
