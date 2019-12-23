package com.example.margat.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.margat.R
import com.example.margat.Test2Activity
import com.example.margat.domain.Member
import com.example.margat.service.MemberService
import com.example.margat.util.RetrofitAPI
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkIfAutoLoginSet()

        forgotPassword.setOnClickListener{
            val intent = Intent(applicationContext, FindPasswordActivity::class.java)
            startActivity(intent)
        }

        signin_button.setOnClickListener {
            var member = Member(email_input.text.toString(), password_input.text.toString())
            var service = RetrofitAPI().creater.create(MemberService::class.java)
            checkEmailOf(member, service)
        }

        signUpText.setOnClickListener{
            val intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
        }

        temp.setOnClickListener{
            val intent = Intent(applicationContext, Test2Activity::class.java)
            startActivity(intent)
        }

    }

    private fun checkEmailOf(member: Member, service: MemberService) {
        service.findMemberByEmail(member).enqueue(object: Callback<Array<Member>> {
            override fun onFailure(call: Call<Array<Member>>, t: Throwable) {
                Toast.makeText(applicationContext, "통신 오류!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Array<Member>>, response: Response<Array<Member>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(applicationContext,
                            "로그인 실패: 이메일 틀립니다!", Toast.LENGTH_SHORT).show()
                        return
                    }
                    checkEmailAndPasswordOf(member, service)
                }
            }
        })
    }
    private fun checkEmailAndPasswordOf(member: Member, service: MemberService) {
        service.findMemberByEmailAndPassword(member).enqueue(object: Callback<Array<Member>> {
            override fun onFailure(call: Call<Array<Member>>, t: Throwable) {
                Toast.makeText(applicationContext, "통신 오류!", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Array<Member>>, response: Response<Array<Member>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(applicationContext,
                            "로그인 실패: 비밀번호가 틀립니다!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if(rememberMe.isChecked)
                        setAutoLogin(member)

                    var name: String = response.body()!![0].name
                    Toast.makeText(applicationContext,
                        "${name}님 환영합니다!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.putExtra("name", name)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        })
    }

    private fun checkIfAutoLoginSet() {
        var info: SharedPreferences = getSharedPreferences("setting", 0)

        if (!info.getString("password", "").equals("")) {
            println("자동로그인이 되어있음")
            var member = Member(info.getString("email", "")!!, info.getString("password", "")!!)
            var service = RetrofitAPI().creater.create(MemberService::class.java)
            checkEmailAndPasswordOf(member, service)
        }
    }

    private fun setAutoLogin(member: Member) {
        var info: SharedPreferences = getSharedPreferences("setting", 0)
        var editor: SharedPreferences.Editor = info.edit()
        editor.putString("email", member.email)
        editor.putString("password", member.password)
        editor.commit()
    }

}
