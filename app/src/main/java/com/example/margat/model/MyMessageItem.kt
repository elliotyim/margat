package com.example.margat.model

import java.sql.Date

object MyMessageItem {

    var messageItemList = ArrayList<MessageItem>()

    data class MessageItem (
        var chatNo: Int,
        var messageUserPhotoItem: String,
        var userName: String,
        var unreadMsgCount: Int,
        var receivedDate: Date,
        var messageContent: String
    )
}