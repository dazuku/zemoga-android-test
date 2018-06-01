package com.zemoga.danieldaza.zemogatest.MainView

import android.arch.lifecycle.ViewModelProviders
import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.os.Bundle
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

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        var serverComunicator = ServerComunicator.getInstance(this.applicationContext)

        if (postsViewModel.getPosts() == null) {
            serverComunicator.getPosts(
                    Response.Listener { response ->
                        Log.i("Server Comunicator", response.toString())
                        postsViewModel.setPosts(Posts.fromJsonArray(response))
                        favoritesItems = postsViewModel.getPosts()
                        allItems = postsViewModel.getPosts()
                        setAdapter()
                    },
                    Response.ErrorListener { error ->
                        Log.e("Server Comunicator", error.toString())
                    })
        } else {
            favoritesItems = postsViewModel.getPosts()
            allItems = postsViewModel.getPosts()
            setAdapter()
        }

    }

    override fun onListFragmentInteraction(item: Post?) {
        Log.i("Holi", item.toString())
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

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val fragment = PostFragment()
            val args = Bundle()

            args.putString("type", if (position == 0) MainActivity.ALL_TAB else MainActivity.FAVORITE_TAB)
            fragment.arguments = args

            return fragment
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
