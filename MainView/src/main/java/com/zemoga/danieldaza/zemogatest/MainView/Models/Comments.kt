package com.zemoga.danieldaza.zemogatest.MainView.Models

import org.json.JSONArray

class Comments : ArrayList<Comment>() {
    companion object {
        fun fromJsonArray(array : JSONArray): Comments {
            val comments = Comments();
            for (i: Int in 0..(array.length() - 1)) {
                val comment = Comment.fromJsonObject(array.getJSONObject(i))
                comments.add(comment)
            }

            return comments
        }
    }
}