package com.zemoga.danieldaza.zemogatest.MainView.ViewModels

import android.arch.lifecycle.ViewModel
import com.zemoga.danieldaza.zemogatest.MainView.Models.Posts

class PostsViewModel : ViewModel() {
    private var posts : Posts?= null

    fun getPosts() : Posts? {
        return this.posts
    }

    fun setPosts(posts : Posts) {
        this.posts = posts
    }
}