package com.example.margat.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.margat.R
import com.example.margat.domain.Member
import com.example.margat.controller.MemberController
import com.example.margat.util.RetrofitAPI
import kotlinx.android.synthetic.main.activity_registration.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        createIDButton.setOnClickListener {

            var member = Member().apply {
                name = nameInput.text.toString()
                password = passwordInput.text.toString()
                email = emailInput.text.toString()
                tel = telInput.text.toString()

            }

            var service = RetrofitAPI().creater.create(MemberController::class.java)
            service.insert(member).enqueue(object: Callback<ResponseBody>{

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(applicationContext, "통신 오류!", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() == 200) {
                        Toast.makeText(applicationContext, "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()

                        val intent = Intent(applicationContext, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }

            })

        }


    }
}
