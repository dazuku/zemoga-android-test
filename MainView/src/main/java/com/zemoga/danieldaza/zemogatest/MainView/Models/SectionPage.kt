package com.zemoga.danieldaza.zemogatest.MainView.Models

import com.zemoga.danieldaza.zemogatest.MainView.Fragments.PostFragment

data class SectionPage(val type: String, private val elements: Posts?) {
    var posts = elements
    var instance: PostFragment? = null

    fun getFragment(): PostFragment? {
        instance = PostFragment.newInstance(type)
        return instance
    }
}