package com.zemoga.danieldaza.zemogatest.MainView

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.android.volley.Response
import com.zemoga.danieldaza.zemogatest.MainView.Fragments.CommentFragment
import com.zemoga.danieldaza.zemogatest.MainView.Models.Comment
import com.zemoga.danieldaza.zemogatest.MainView.Models.Comments
import com.zemoga.danieldaza.zemogatest.MainView.Models.Post
import com.zemoga.danieldaza.zemogatest.MainView.Models.User
import com.zemoga.danieldaza.zemogatest.MainView.Utils.ServerCommunicator
import com.zemoga.danieldaza.zemogatest.MainView.ViewModels.PostsViewModel
import kotlinx.android.synthetic.main.activity_post_detail.*

class PostDetailActivity : AppCompatActivity(), CommentFragment.OnListFragmentInteractionListener {
    private var userModel: User? = null
    private var commentsModel: Comments? = null
    private var postModel: Post? = null

    companion object {
        const val IS_FAVORITE = "IS_FAVORITE"
        const val POST_ID = "POST_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        this.postModel = intent?.getSerializableExtra("post") as Post

        var postsViewModel = ViewModelProviders.of(this).get(PostsViewModel::class.java)
        val serverCommunicator = ServerCommunicator(this.applicationContext)

        this.description.text = postModel?.body

        if (postsViewModel.getComments(postModel?.id) == null) {
            serverCommunicator.getCommentsByPostId(postModel?.id, Response.Listener { response ->
                this.commentsModel = Comments.fromJsonArray(response)
                postsViewModel.setComments(postModel?.id, this.commentsModel as Comments)
                this.invalidateComments()
            }, Response.ErrorListener { error ->
                Log.e("Server Communicator", error.toString())
            })
        }

        if (postsViewModel.getUser(postModel?.userId) == null) {
            serverCommunicator.getUserById(postModel?.userId, Response.Listener { response ->
                this.userModel = User.fromJsonObject(response)
                postsViewModel.setUser(postModel?.userId, this.userModel!!)
                this.invalidateUser()
            }, Response.ErrorListener { error ->
                Log.e("Server Communicator", error.toString())
            })
        } else {
            this.userModel = postsViewModel.getUser(postModel?.userId)
            this.invalidateUser()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_post_detail, menu)

        val actionIcon = getActionIcon()
        val favItem = menu.getItem(0)

        favItem.icon = ContextCompat.getDrawable(this, actionIcon)
        return true
    }

    private fun getActionIcon(): Int {
        val isFavorite : Boolean = if (this.postModel == null) false else this.postModel?.isFavorite() as Boolean
        return if (isFavorite) R.drawable.ic_star_black_24dp else R.drawable.ic_star_border_black_24dp
    }

    override fun finish() {
        intent.putExtra(PostDetailActivity.POST_ID, this.postModel?.id)
        intent.putExtra(PostDetailActivity.IS_FAVORITE, this.postModel?.isFavorite())
        setResult(Activity.RESULT_OK, intent)

        super.finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }

        if (item.itemId == R.id.action_favorite) {
            this.postModel?.toggleFavorite()
            val actionIcon = getActionIcon()
            item.icon = ContextCompat.getDrawable(this, actionIcon)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onListFragmentInteraction(item: Comment?) {

    }

    private fun invalidateComments() {
        val fragment = CommentFragment.newInstance(this.commentsModel as Comments) as android.support.v4.app.Fragment
        val ft = supportFragmentManager.beginTransaction()
        ft.add(R.id.commentFragment, fragment).commit()

        progressBarComments.visibility = View.GONE
    }

    private fun invalidateUser() {
        this.name.text = this.userModel?.name
        this.email.text = this.userModel?.email
        this.phone.text = this.userModel?.phone
        this.website.text = this.userModel?.website

        progressBarUserInfo.visibility = View.GONE
    }
}
