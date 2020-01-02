package com.example.margat.model

data class Member(
    var no: Int = 0,
    var name: String? = null,
    var password: String? = null,
    var email: String? = null,
    var tel: String? = null,
    var registeredDate: String? = null,
    var profilePhoto: String? = null,
    var emailKey: String? = null,
    var memberState: Int = 0
)