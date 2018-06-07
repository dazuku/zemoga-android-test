package com.zemoga.danieldaza.zemogatest.MainView

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.Response
import com.zemoga.danieldaza.zemogatest.MainView.Fragments.PostFragment
import com.zemoga.danieldaza.zemogatest.MainView.Models.Post
import com.zemoga.danieldaza.zemogatest.MainView.Models.Posts
import com.zemoga.danieldaza.zemogatest.MainView.Models.SectionPage
import com.zemoga.danieldaza.zemogatest.MainView.Utils.ServerCommunicator
import com.zemoga.danieldaza.zemogatest.MainView.ViewModels.PostsViewModel

import kotlinx.android.synthetic.main.activity_main.*

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
    private var sections: HashMap<String, SectionPage> = HashMap()
    private var postsViewModel: PostsViewModel? = null
    private var serverCommunicator: ServerCommunicator? = null

    private val keys = ArrayList<String>()


    init {
        keys.add(ALL_TAB)
        keys.add(FAVORITE_TAB)
    }

    companion object {
        const val FAVORITE_TAB = "FAVORITE_TAB"
        const val ALL_TAB = "ALL_TAB"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        serverCommunicator = ServerCommunicator.getInstance(applicationContext)

        if (postsViewModel?.getPosts() == null) {

            serverCommunicator?.getPosts(
                    Response.Listener { response ->
                        val posts = Posts.fromJsonArray(response)

                        for (i in 0..19) {
                            posts[i].setUnread()
                        }

                        postsViewModel?.setPosts(posts)
                        progressBarPosts.visibility = View.GONE

                        setSections()
                        setAdapter()
                    },
                    Response.ErrorListener { error ->
                        Log.e("Server Communicator", error.toString())
                    })
        } else {
            setSections()
            setAdapter()
        }

        removeAll.setOnClickListener { view ->
            mSectionsPagerAdapter!!.sections[ALL_TAB]?.instance?.removeAll()
            setFavoriteItems()
            mSectionsPagerAdapter?.notifyDataSetChanged()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            val postId = data?.getIntExtra(PostDetailActivity.POST_ID, 0)
            val favoriteValue = data?.getBooleanExtra(PostDetailActivity.IS_FAVORITE, false)
            val post = sections[ALL_TAB]?.posts?.find { post -> post.id == postId }

            post?.setRead()
            post?.setFavorite(favoriteValue as Boolean)
            setFavoriteItems()

            mSectionsPagerAdapter?.notifyDataSetChanged()
        }
    }

    override fun onListFragmentInteraction(item: Post?): Boolean {
        val intent = Intent(this, PostDetailActivity::class.java).apply {
            putExtra("description", item?.body)
            putExtra("id", item?.id)
            putExtra("userId", item?.userId)
            putExtra("post", item)
        }

        startActivityForResult(intent, 1)

        return false
    }

    override fun onRemoveItemFromListListener(position: Int, type: String) {
        if (type == FAVORITE_TAB) {
            val postToDelete = sections[FAVORITE_TAB]?.posts!![position]
            val index = sections[ALL_TAB]?.posts?.indexOf(postToDelete)

            sections[ALL_TAB]?.posts?.removeAt(index as Int)
        } else {
            setFavoriteItems()
        }

        mSectionsPagerAdapter?.notifyDataSetChanged()
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
            progressBarPosts.visibility = View.VISIBLE

            serverCommunicator?.getPosts(
                    Response.Listener { response ->
                        val posts = Posts.fromJsonArray(response)

                        for (i in 0..19) {
                            posts[i].setUnread()
                        }

                        postsViewModel?.setPosts(posts)
                        sections[FAVORITE_TAB]?.posts = Posts()
                        sections[ALL_TAB]?.posts = postsViewModel?.getPosts()
                        mSectionsPagerAdapter?.notifyDataSetChanged()
                        progressBarPosts.visibility = View.GONE
                    },
                    Response.ErrorListener { error ->
                        Log.e("Server Communicator", error.toString())
                    })

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setSections() {
        sections[ALL_TAB] = SectionPage(ALL_TAB, postsViewModel?.getPosts())
        sections[FAVORITE_TAB] = SectionPage(FAVORITE_TAB, Posts())
    }

    private fun setAdapter() {
        setSupportActionBar(toolbar)
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = SectionsPagerAdapter(sections, keys, supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }

    private fun setFavoriteItems() {
        val favorites = Posts()

        for (post in sections[ALL_TAB]?.posts!!) {
            if (post.isFavorite()) favorites.add(post)
        }

        sections[FAVORITE_TAB]?.posts = favorites
    }


    fun getFragmentData(type: String) : Posts? {
        return sections[type]?.posts
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(
            var sections: HashMap<String, SectionPage>,
            private var keys: ArrayList<String>,
            fm: FragmentManager): FragmentPagerAdapter(fm) {

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        override fun getItem(position: Int): Fragment {
            val type = keys[position]
            return sections[type]?.getFragment() as Fragment
        }

        override fun getCount(): Int {
            return keys.size
        }
    }
}
