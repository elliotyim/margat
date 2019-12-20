package com.example.margat.domain

class Member {
    var no: Int = 0
    lateinit var name: String
    lateinit var password: String
    lateinit var email: String
    lateinit var tel: String
    lateinit var profilePhoto: String

    constructor()

    constructor(_email: String, _password: String) {
        email = _email
        password = _password
    }

}