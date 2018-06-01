package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONObject

class Post {
    private var userId : Int ?= null
    private var id : Int ?= null
    private var title : String ?= ""
    private var body : String ?= ""

    constructor(userId: Int, id: Int, title: String, body: String) {
        this.userId = userId
        this.id = id
        this.title = title
        this.body = body
    }

    companion object {
        fun fromJsonObject(json : JSONObject) : Post {
            val userId = json.get("userId") as Int
            val id = json.get("id") as Int
            val title = json.get("title") as String
            val body = json.get("body") as String

            return Post(userId = userId, id = id, title = title, body = body)
        }
    }

    fun getUserId() : Int? {
        return this.userId
    }

    fun getId() : Int? {
        return this.id
    }

    fun getTitle() : String? {
        return this.title
    }

    fun getBody() : String? {
        return this.body
    }

    fun setUserId(userId : Int) {
        this.userId = userId
    }

    fun setId(id : Int) {
        this.id = id
    }

    fun setTitle(title: String) {
        this.title = title
    }

    fun setBody(body: String) {
        this.body = body
    }
}