package com.zemoga.danieldaza.zemogatest.MainView

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.android.volley.Response
import com.zemoga.danieldaza.zemogatest.MainView.Models.Post
import com.zemoga.danieldaza.zemogatest.MainView.Models.Posts
import com.zemoga.danieldaza.zemogatest.MainView.Utils.ServerComunicator
import com.zemoga.danieldaza.zemogatest.MainView.ViewModels.PostsViewModel

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainActivity : AppCompatActivity(), PostFragment.OnListFragmentInteractionListener {
    /**
     * The [android.support.v4.view.PagerAdapter] that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * [android.support.v4.app.FragmentStatePagerAdapter].
     */
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    companion object {
        val FAVORITE_TAB = "FAVORITE_TAB"
        val ALL_TAB = "ALL_TAB"
    }

    private var favoritesItems: Posts? = null
    private var allItems: Posts? = null

    fun setAdapter() {
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        removeAll.setOnClickListener { view ->
            mSectionsPagerAdapter!!.fragments[0].removeAll()
            setFavoriteItems()
            mSectionsPagerAdapter?.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)

        if (postsViewModel.getPosts() == null) {
            val serverComunicator = ServerComunicator.getInstance(this.applicationContext)

            serverComunicator.getPosts(
                    Response.Listener { response ->
                        Log.i("Server Comunicator", response.toString())
                        val posts = Posts.fromJsonArray(response)

                        for (i in 0..19) {
                            posts[i].setUnread()
                        }

                        postsViewModel.setPosts(posts)
                        favoritesItems = Posts()
                        allItems = postsViewModel.getPosts()
                        progressBarPosts.visibility = View.GONE
                        setAdapter()
                    },
                    Response.ErrorListener { error ->
                        Log.e("Server Comunicator", error.toString())
                    })
        } else {
            favoritesItems = Posts()
            allItems = postsViewModel.getPosts()
            setAdapter()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val postId = data?.getIntExtra(PostDetailActivity.POST_ID, 0)
            val favoriteValue = data?.getBooleanExtra(PostDetailActivity.IS_FAVORITE, false)

            val post = allItems?.find { post -> post.id == postId }
            post?.setFavorite(favoriteValue as Boolean)

            setFavoriteItems()

            mSectionsPagerAdapter?.notifyDataSetChanged()
        }
    }

    fun setFavoriteItems() {
        val favorites = Posts()

        for (post in allItems!!) {
            if (post.isFavorite()) favorites.add(post)
        }

        this.favoritesItems = favorites
    }

    override fun onListFragmentInteraction(item: Post?): Boolean {
        val intent = Intent(this, PostDetailActivity::class.java).apply {
            putExtra("description", item?.body)
            putExtra("id", item?.id)
            putExtra("userId", item?.userId)
            putExtra("post", item)
        }

        startActivityForResult(intent, 1)
        item?.setRead()

        return true
    }

    override fun onRemoveItemFromListListener(position: Int, type: String) {
        if (type == FAVORITE_TAB) {
            val postToDelete = favoritesItems!![position]
            val index = allItems?.indexOf(postToDelete)

            allItems?.removeAt(index as Int)
        } else {
            this.setFavoriteItems()
        }

        mSectionsPagerAdapter?.notifyDataSetChanged()
    }


    fun getFragmentData(type: String) : Posts? {
        if (type === MainActivity.FAVORITE_TAB) {
            return favoritesItems
        } else if (type === MainActivity.ALL_TAB) {
            return allItems
        }
        return null
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_reload) {
            val postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
            val serverComunicator = ServerComunicator.getInstance(this.applicationContext)

            progressBarPosts.visibility = View.VISIBLE

            serverComunicator.getPosts(
                    Response.Listener { response ->
                        Log.i("Server Comunicator", response.toString())
                        val posts = Posts.fromJsonArray(response)

                        for (i in 0..19) {
                            posts[i].setUnread()
                        }

                        postsViewModel.setPosts(posts)
                        favoritesItems = Posts()
                        allItems = postsViewModel.getPosts()
                        mSectionsPagerAdapter?.notifyDataSetChanged()
                        progressBarPosts.visibility = View.GONE
                    },
                    Response.ErrorListener { error ->
                        Log.e("Server Comunicator", error.toString())
                    })

            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        var fragments: ArrayList<PostFragment> = ArrayList(2)

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            val type = if (position == 0) MainActivity.ALL_TAB else MainActivity.FAVORITE_TAB

            fragments.add(position, PostFragment.newInstance(type))

            return fragments[position]
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
