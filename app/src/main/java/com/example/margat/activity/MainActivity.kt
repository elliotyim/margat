package com.example.margat.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.margat.R
import kotlinx.android.synthetic.main.activity_landing.*

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)

        successText.text = intent?.getStringExtra("name") + "님 환영합니다!"
        logoutButton.setOnClickListener {
            checkout()
            val intent = Intent(applicationContext, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun checkout() {
        var info: SharedPreferences = getSharedPreferences("setting", 0)
        var editor: SharedPreferences.Editor = info.edit()
        editor.clear()
        editor.commit()
    }
}
