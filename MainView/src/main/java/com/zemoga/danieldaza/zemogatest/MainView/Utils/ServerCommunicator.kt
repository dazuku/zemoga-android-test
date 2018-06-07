package com.zemoga.danieldaza.zemogatest.MainView.Utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ServerCommunicator constructor(context : Context) {
    companion object {
        @Volatile
        private var INSTANCE : ServerCommunicator ?= null
        fun getInstance(context : Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: ServerCommunicator(context)
                }
    }

    private val endpoint = "https://jsonplaceholder.typicode.com"

    private val requestQueue : RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getPosts(successCallback : Response.Listener<JSONArray>, errorCallback : Response.ErrorListener) {
        val jsonRequest = JsonArrayRequest(Request.Method.GET, "$endpoint/posts", null, successCallback, errorCallback)

        requestQueue.add(jsonRequest)
    }

    fun getCommentsByPostId(id: Int?, successCallback: Response.Listener<JSONArray>, errorCallback: Response.ErrorListener) {
        val jsonRequest = JsonArrayRequest(Request.Method.GET, "$endpoint/posts/$id/comments", null, successCallback, errorCallback)

        requestQueue.add(jsonRequest)
    }

    fun getUserById(id: Int?, successCallback: Response.Listener<JSONObject>, errorCallback: Response.ErrorListener) {
        val jsonRequest = JsonObjectRequest(Request.Method.GET, "$endpoint/users/$id", null, successCallback, errorCallback)

        requestQueue.add(jsonRequest)
    }
}