package com.zemoga.danieldaza.zemogatest.MainView

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zemoga.danieldaza.zemogatest.MainView.Models.Comment
import com.zemoga.danieldaza.zemogatest.MainView.Models.Comments

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [CommentFragment.OnListFragmentInteractionListener] interface.
 */
class CommentFragment : Fragment() {

    private var listener: OnListFragmentInteractionListener? = null
    private var comments: Comments? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            comments = it.getSerializable(CommentFragment.ARG_COMMENTS) as Comments
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_comment_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MyCommentRecyclerViewAdapter(comments as Comments, listener)
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun updateComments(comments: Comments) {
        val view = this.view as RecyclerView
        val adapter = view.adapter as MyCommentRecyclerViewAdapter

        adapter.updateComments(comments)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Comment?)
    }

    companion object {
        const val ARG_COMMENTS = "comments"

        @JvmStatic
        fun newInstance(comments: Comments) =
                CommentFragment().apply {
                    arguments = Bundle().apply {
                        putSerializable(ARG_COMMENTS, comments)
                    }
                }
    }
}
