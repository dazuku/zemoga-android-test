package com.zemoga.danieldaza.zemogatest.MainView.ViewModels

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.zemoga.danieldaza.zemogatest.MainView.Models.Comments
import com.zemoga.danieldaza.zemogatest.MainView.Models.Posts
import com.zemoga.danieldaza.zemogatest.MainView.Models.User

class PostsViewModel : ViewModel() {
    private var posts : Posts? = null
    private var comments : HashMap<Int?, Comments>? = HashMap()
    private var users : HashMap<Int?, User>? = HashMap()

    fun getComments(id: Int?): Comments? {
        return this.comments?.get(id)
    }

    fun setComments(id: Int?, comments: Comments) {
        this.comments?.set(id, comments)
    }

    fun getUser(id: Int?): User? {
        return this.users?.get(id)
    }

    fun setUser(id: Int?, user: User) {
        this.users?.set(id, user)
    }

    fun getPosts(): Posts? {
        return this.posts
    }

    fun setPosts(posts: Posts) {
        this.posts = posts
    }
}