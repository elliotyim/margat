package com.example.margat.item

import java.sql.Date

data class MessageItem (
    var chatNo: Int,
    var messageUserPhotoItem: String,
    var userName: String,
    var unreadMsgCount: Int,
    var receivedDate: Date,
    var messageContent: String
)