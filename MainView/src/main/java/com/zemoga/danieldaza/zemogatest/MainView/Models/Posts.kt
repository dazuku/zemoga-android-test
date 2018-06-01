package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONArray

class Posts : ArrayList<Post>() {
    companion object {
        fun fromJsonArray(array : JSONArray) : Posts {
            val posts = Posts();
            for (i: Int in 0..(array.length() - 1)) {
                val post = Post.fromJsonObject(array.getJSONObject(i))
                posts.add(post)
            }

            return posts
        }
    }
}