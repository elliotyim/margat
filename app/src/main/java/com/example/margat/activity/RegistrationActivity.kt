package com.example.margat.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.margat.R
import com.example.margat.model.Member
import com.example.margat.request.MemberRequest
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import kotlinx.android.synthetic.main.activity_registration.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        setOnClickListenerToCreateIDButton()
    }

    private fun setOnClickListenerToCreateIDButton() {
        createIDButton.setOnClickListener {
            var member = Member().apply {
                name = nameInput.text.toString()
                password = passwordInput.text.toString()
                email = emailInput.text.toString()
                tel = telInput.text.toString()
            }
            requestUserRegistrationOf(member)
        }
    }

    private fun requestUserRegistrationOf(member: Member) {
        RetrofitAPI.newInstance().getRetrofit().create(MemberRequest::class.java)
            .insert(member).enqueue(object: MyCallback<ResponseBody>() {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    Toast.makeText(applicationContext,
                        "회원가입이 완료되었습니다!", Toast.LENGTH_SHORT).show()
                    goBackToLoginActivity()
                }
            }
        })
    }

    private fun goBackToLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)

    }
}
