package com.example.margat.domain

data class Photo(
    var photoNo: Int = 0,
    var postNo: Int = 0,
    var messageNo: Int = 0,
    var photoName: String? = null,
    var createdDate: String? = null
)