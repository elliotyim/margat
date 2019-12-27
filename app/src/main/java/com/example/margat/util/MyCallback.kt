package com.example.margat.util

import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class MyCallback<T : Any?> : Callback<T> {
    override fun onFailure(call: Call<T>, t: Throwable) {
        Toast.makeText(App.MyApp.getAppContext(), "통신 오류!", Toast.LENGTH_SHORT).show()
    }

    abstract override fun onResponse(call: Call<T>, response: Response<T>)

}