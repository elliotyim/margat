package com.example.margat.model

data class Post(
    var postNo: Int = 0,
    var memberNo: Int = 0,
    var postContent: String? = null,
    var postCreatedDate: String? = null,

    var photos: List<Photo> = listOf()
)
