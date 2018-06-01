package com.zemoga.danieldaza.zemogatest.MainView.Utils

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class ServerComunicator constructor(context : Context) {
    companion object {
        @Volatile
        private var INSTANCE : ServerComunicator ?= null
        fun getInstance(context : Context) =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: ServerComunicator(context)
                }
    }

    private val endpoint = "https://jsonplaceholder.typicode.com"

    private val requestQueue : RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }

    fun getPosts(sucessCallback : Response.Listener<JSONArray>, errorCallback : Response.ErrorListener) {
        val jsonRequest = JsonArrayRequest(Request.Method.GET, "$endpoint/posts", null, sucessCallback, errorCallback)

        requestQueue.add(jsonRequest);
    }
}