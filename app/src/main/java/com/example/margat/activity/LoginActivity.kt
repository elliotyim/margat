package com.example.margat.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.margat.R
import com.example.margat.model.Member
import com.example.margat.request.MemberRequest
import com.example.margat.util.MyCallback
import com.example.margat.util.RetrofitAPI
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        checkIfAutoLoginSet()

        setOnClickListenerToForgotPasswordButton()
        setOnclickListenerToSignUpButton()
        setOnClickListenerToSignInButton()
    }

    private fun checkIfAutoLoginSet() {
        var info: SharedPreferences = getSharedPreferences("autoLoginUser", 0)

        if (!info.getString("password", "").equals("")) {
            var member = Member().apply {
                email = info.getString("email", "").toString()
                password = info.getString("password", "").toString()
            }

            checkEmailAndPasswordOf(member, RetrofitAPI.newInstance().getRetrofit().create(MemberRequest::class.java))
        }
    }

    private fun setOnClickListenerToForgotPasswordButton() {
        forgotPassword.setOnClickListener{
            startActivity(Intent(applicationContext, FindPasswordActivity::class.java))
        }
    }

    private fun setOnclickListenerToSignUpButton() {
        signUpText.setOnClickListener{
            startActivity(Intent(applicationContext, RegistrationActivity::class.java))
        }
    }

    private fun setOnClickListenerToSignInButton() {
        signin_button.setOnClickListener {
            var member = Member().apply {
                email = email_input.text.toString()
                password = password_input.text.toString()
            }

            checkEmailOf(member, RetrofitAPI.newInstance().getRetrofit().create(MemberRequest::class.java))
        }

    }

    private fun checkEmailOf(member: Member, memberRequest: MemberRequest) {
        memberRequest.findMemberByEmail(member).enqueue(object: MyCallback<ArrayList<Member>>() {
            override fun onResponse(call: Call<ArrayList<Member>>, response: Response<ArrayList<Member>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(applicationContext,
                            "로그인 실패: 이메일 틀립니다!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    checkEmailAndPasswordOf(member, memberRequest)
                }
            }
        })
    }
    private fun checkEmailAndPasswordOf(member: Member, memberRequest: MemberRequest) {
        memberRequest.findMemberByEmailAndPassword(member).enqueue(object: MyCallback<ArrayList<Member>>() {
            override fun onResponse(call: Call<ArrayList<Member>>, response: Response<ArrayList<Member>>) {
                if (response.code() == 200) {
                    if (response.body().isNullOrEmpty()) {
                        Toast.makeText(applicationContext,
                            "로그인 실패: 비밀번호가 틀립니다!", Toast.LENGTH_SHORT).show()
                        return
                    }

                    if (rememberMe.isChecked) setAutoLogin(member)

                    var member: Member? = response.body()!![0]
                    Toast.makeText(applicationContext,
                        "${member!!.name}님 환영합니다!", Toast.LENGTH_SHORT).show()

                    var info = getSharedPreferences("loginUser", 0)
                    var editor = info.edit().apply {
                        putInt("no", member!!.no)
                        putString("name", member!!.name)
                        putString("email", member!!.email)
                        putString("tel", member!!.tel)
                        putString("registeredDate", member!!.registeredDate)
                        putString("profilePhoto", member!!.profilePhoto)
                        putString("emailKey", member!!.emailKey)
                        putInt("memberState", member!!.memberState)
                    }
                    editor.commit()

                    member = null
                    goToMainActivity()
                }
            }
        })
    }

    private fun goToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }



    private fun setAutoLogin(member: Member) {
        var info: SharedPreferences = getSharedPreferences("autoLoginUser", 0)
        var editor: SharedPreferences.Editor = info.edit()

        editor.putString("email", member.email)
        editor.putString("password", member.password)
        editor.commit()
    }

}
