package com.example.margat.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.margat.R
import com.example.margat.domain.Member
import com.example.margat.controller.MemberController
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkIfAutoLoginSet()

        forgotPassword.setOnClickListener{
            val intent = Intent(applicationContext, FindPasswordActivity::class.java)
            startActivity(intent)
        }

        signin_button.setOnClickListener {
            var member = Member().apply {
                email = email_input.text.toString()
                password = password_input.text.toString()
            }

            var controller = RetrofitAPI().creater.create(MemberController::class.java)
            checkEmailOf(member, controller)
        }

        signUpText.setOnClickListener{
            val intent = Intent(applicationContext, RegistrationActivity::class.java)
            startActivity(intent)
        }

    }

    private fun checkEmailOf(member: Member, controller: MemberController) {
        controller.findMemberByEmail(member).enqueue(object: MyCallback<Array<Member>>() {
            override fun onResponse(call: Call<Array<Member>>, response: Response<Array<Member>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(applicationContext,
                            "로그인 실패: 이메일 틀립니다!", Toast.LENGTH_SHORT).show()
                        return
                    }
                    checkEmailAndPasswordOf(member, controller)
                }
            }
        })
    }
    private fun checkEmailAndPasswordOf(member: Member, controller: MemberController) {
        controller.findMemberByEmailAndPassword(member).enqueue(object: MyCallback<Array<Member>>() {
            override fun onResponse(call: Call<Array<Member>>, response: Response<Array<Member>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(applicationContext,
                            "로그인 실패: 비밀번호가 틀립니다!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if(rememberMe.isChecked)
                        setAutoLogin(member)

                    var name: String? = response.body()!![0].name
                    Toast.makeText(applicationContext,
                        "${name}님 환영합니다!", Toast.LENGTH_SHORT).show()
                    var info = getSharedPreferences("loginUser", 0)
                    var editor = info.edit().apply {
                        putInt("no", response.body()!![0].no)
                        putString("name", name)
                        putString("email", response.body()!![0].email)
                        putString("tel", response.body()!![0].tel)
                        putString("registeredDate", response.body()!![0].registeredDate)
                        putString("profilePhoto", response.body()!![0].profilePhoto)
                        putString("emailKey", response.body()!![0].emailKey)
                        putInt("memberState", response.body()!![0].memberState)
                    }
                    editor.commit()

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
            var member = Member().apply {
                email = info.getString("email", "").toString()
                password = info.getString("password", "").toString()
            }
            var controller = RetrofitAPI().creater.create(MemberController::class.java)
            checkEmailAndPasswordOf(member, controller)
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
