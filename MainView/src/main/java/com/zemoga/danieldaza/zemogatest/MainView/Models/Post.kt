package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONObject
import java.io.Serializable

data class Post (val userId: Int, val id: Int, val title: String, val body: String) : Serializable {
    private var favorite: Boolean = false
    private var read: Boolean = true

    companion object {
        fun fromJsonObject(json : JSONObject) : Post {
            val userId = json.get("userId") as Int
            val id = json.get("id") as Int
            val title = json.get("title") as String
            val body = json.get("body") as String

            return Post(userId = userId, id = id, title = title.capitalize(), body = body.capitalize())
        }
    }

    fun setFavorite(favorite: Boolean) {
        this.favorite = favorite
    }

    fun toggleFavorite() {
        this.favorite = !this.favorite
    }

    fun isRead(): Boolean {
        return this.read
    }

    fun setUnread() {
        this.read = false
    }

    fun setRead() {
        this.read = true
    }

    fun isFavorite(): Boolean {
        return this.favorite
    }

}