package com.example.margat.item

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object MessageContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<MessageItem> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, MessageItem> = HashMap()

    private val COUNT = 3

    init {
        // Add some sample items.
        for (i in 1..COUNT) {
            addItem(
                createMessageItem(
                    i
                )
            )
        }
    }

    private fun addItem(item: MessageItem) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    private fun createMessageItem(position: Int): MessageItem {
        return MessageItem(
            position.toString(),
            "Item " + position,
            makeDetails(position)
        )
    }

    private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }

    /**
     * A dummy item representing a piece of content.
     */
    data class MessageItem(val id: String, val content: String, val details: String) {
        override fun toString(): String = content
    }
}
