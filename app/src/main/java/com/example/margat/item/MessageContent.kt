package com.example.margat.item

import java.util.ArrayList
import java.util.HashMap

object MessageContent {
    val ITEMS: MutableList<MessageItem> = ArrayList()
    private val ITEM_MAP: MutableMap<String, MessageItem> = HashMap()
    private const val COUNT = 3

    init {
        for (i in 1..COUNT)
            addItem(createMessageItem(i))
    }

    private fun addItem(item: MessageItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createMessageItem(position: Int): MessageItem {
        return MessageItem(
            position.toString(),
            "Item $position",
            makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0 until position) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    data class MessageItem(
        val id: String,
        val content: String,
        val details: String) {
        override fun toString(): String = content
    }
}
