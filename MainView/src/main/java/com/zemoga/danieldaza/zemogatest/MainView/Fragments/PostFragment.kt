package com.zemoga.danieldaza.zemogatest.MainView.Fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.zemoga.danieldaza.zemogatest.MainView.Adapters.MyPostRecyclerViewAdapter
import com.zemoga.danieldaza.zemogatest.MainView.Listeners.DeleteSwipeListener
import com.zemoga.danieldaza.zemogatest.MainView.MainActivity
import com.zemoga.danieldaza.zemogatest.MainView.Models.Post
import com.zemoga.danieldaza.zemogatest.MainView.Models.Posts
import com.zemoga.danieldaza.zemogatest.MainView.R

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [PostFragment.OnListFragmentInteractionListener] interface.
 */
class PostFragment : Fragment() {
    private var type = ""
    private var listener: OnListFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            type = it.getString(ARG_TYPE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post_list, container, false)
        val bundle = this.arguments
        val type = bundle?.getString("type")
        val items = (activity as MainActivity).getFragmentData(type as String)

        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = LinearLayoutManager(context)
                adapter = MyPostRecyclerViewAdapter(items as Posts, listener)
            }

            val deleteSwipeHandler = object : DeleteSwipeListener(this.context as Context) {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
                    val position = viewHolder!!.adapterPosition
                    val adapter = view.adapter as MyPostRecyclerViewAdapter

                    listener?.onRemoveItemFromListListener(position, type)
                    adapter.removeAt(position)
                }
            }

            val itemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            itemTouchHelper.attachToRecyclerView(view)
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

    fun removeAll() {
        val adapter = (view as RecyclerView).adapter as MyPostRecyclerViewAdapter
        adapter.removeAll()
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
        fun onListFragmentInteraction(item: Post?): Boolean
        fun onRemoveItemFromListListener(position: Int, type: String)
    }

    companion object {
        const val ARG_TYPE = "type"

        @JvmStatic
        fun newInstance(type: String) =
                PostFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_TYPE, type)
                    }
                }
    }
}
