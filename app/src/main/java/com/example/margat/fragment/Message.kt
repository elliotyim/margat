package com.example.margat.fragment

import com.example.margat.model.Author
import com.stfalcon.chatkit.commons.models.IMessage
import java.sql.Date

data class Message(
    private val id: String,
    private val text: String,
    private val user: Author,
    private val createdDate: Date): IMessage {

    override fun getId(): String {
        return id
    }

    override fun getText(): String {
        return text
    }

    override fun getUser(): Author {
        return user
    }

    override fun getCreatedAt(): Date {
        return createdDate
    }

}